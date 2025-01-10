package ru.javaops.masterjava.upload;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.dao.UserGroupDao;
import ru.javaops.masterjava.persist.model.*;
import ru.javaops.masterjava.persist.model.type.UserFlag;
import ru.javaops.masterjava.upload.PayloadProcessor.FailedEmails;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
public class UserProcessor {
    private static final int NUMBER_THREADS = 4;

    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
    private static UserDao userDao = DBIProvider.getDao(UserDao.class);
    private static UserGroupDao userGroupDao = DBIProvider.getDao(UserGroupDao.class);

    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREADS);

    /*
     * return failed users chunks
     */
    @Data
    @AllArgsConstructor
    private static class UserTo {
        public User user;
        public List<UserGroup> groups;
    }

    public List<FailedEmails> process(final StaxStreamProcessor processor, Map<String, City> cities, Map<String, Group> groups, int chunkSize) throws XMLStreamException, JAXBException {
        log.info("Start processing with chunkSize=" + chunkSize);

        Map<String, Future<List<String>>> chunkFutures = new LinkedHashMap<>();  // ordered map (emailRange -> chunk future)

        int id = userDao.getSeqAndSkip(chunkSize);
        List<UserTo> chunk = new ArrayList<>(chunkSize);
        val unmarshaller = jaxbParser.createUnmarshaller();
        List<FailedEmails> failed = new ArrayList<>();

        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {

            String cityRef = processor.getAttribute("city");  // unmarshal doesn't get city ref
            String groupsString = processor.getAttribute("groupRefs");
            ru.javaops.masterjava.xml.schema.User xmlUser = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.User.class);

            if (cities.get(cityRef) == null) {
                failed.add(new FailedEmails(xmlUser.getEmail(), "City '" + cityRef + "' is not present in DB"));
            } else {
                List<String> groupRefs = (Strings.isNullOrEmpty(groupsString))
                        ? Collections.emptyList()
                        : Splitter.on(" ").splitToList(groupsString);

                if (!groups.keySet().containsAll(groupRefs)) {
                    failed.add(new FailedEmails(xmlUser.getEmail(), "One of groups " + groupRefs + " is not present in DB"));
                } else {
                    User user = new User(id++, xmlUser.getValue(), xmlUser.getEmail(), UserFlag.valueOf(xmlUser.getFlag().value()), cityRef);
                    List<UserGroup> userGroups = groupRefs.stream()
                            .map(s -> new UserGroup(user.getId(), groups.get(s).getId())).collect(Collectors.toList());
                    final UserTo userTo = new UserTo(user, userGroups);
                    chunk.add(userTo);
                    if (chunk.size() == chunkSize) {
                        addChunkFutures(chunkFutures, chunk);
                        chunk = new ArrayList<>(chunkSize);
                        id = userDao.getSeqAndSkip(chunkSize);
                    }
                }
            }
        }

        if (!chunk.isEmpty()) {
            addChunkFutures(chunkFutures, chunk);
        }

        List<String> allAlreadyPresents = new ArrayList<>();
        chunkFutures.forEach((emailRange, future) -> {
            try {
                List<String> alreadyPresentsInChunk = future.get();
                log.info("{} successfully executed with already presents: {}", emailRange, alreadyPresentsInChunk);
                allAlreadyPresents.addAll(alreadyPresentsInChunk);
            } catch (InterruptedException | ExecutionException e) {
                log.error(emailRange + " failed", e);
                failed.add(new FailedEmails(emailRange, e.toString()));
            }
        });
        if (!allAlreadyPresents.isEmpty()) {
            failed.add(new FailedEmails(allAlreadyPresents.toString(), "already presents"));

        }
        return failed;
    }

    private void addChunkFutures(Map<String, Future<List<String>>> chunkFutures, List<UserTo> chunk) {
        String emailRange = String.format("[%s-%s]", chunk.get(0).getUser().getEmail(), chunk.get(chunk.size() - 1).getUser().getEmail());
        Callable<List<String>> task = () -> {
            List<User> users = chunk.stream().map(userTo -> userTo.user).collect(Collectors.toList());
            List<User> alreadyPresents = userDao.insertAndGetConflictEmails(users);
            Set<Integer> alreadyPresentsIds = alreadyPresents.stream().map(BaseEntity::getId).collect(Collectors.toSet());
            List<UserGroup> notExistedGroups = chunk.stream()
                    .flatMap(userTo -> userTo.getGroups().stream())
                    .filter(userGroup -> !alreadyPresentsIds.contains(userGroup.getUserId()))
                    .collect(Collectors.toList());
            userGroupDao.insertBatch(notExistedGroups);
            return alreadyPresents.stream().map(User::getEmail).collect(Collectors.toList());
        };
        Future<List<String>> future = executorService.submit(task);
        chunkFutures.put(emailRange, future);
        log.info("Submit chunk: " + emailRange);
    }
}
