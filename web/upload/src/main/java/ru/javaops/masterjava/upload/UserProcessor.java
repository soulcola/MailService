package ru.javaops.masterjava.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.JaxbUnmarshaller;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserProcessor {
    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
    private static final UserDao userDao = DBIProvider.getDao(UserDao.class);
    private final ExecutorService executorService = Executors.newFixedThreadPool(8);
    private final Logger log = LoggerFactory.getLogger(UserProcessor.class);

    public List<User> process(final InputStream is, int batchSize) throws XMLStreamException, JAXBException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);
        List<User> users = new ArrayList<>();

        JaxbUnmarshaller unmarshaller = jaxbParser.createUnmarshaller();
        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            ru.javaops.masterjava.xml.schema.User xmlUser = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.User.class);
            final User user = new User(xmlUser.getValue(), xmlUser.getEmail(), UserFlag.valueOf(xmlUser.getFlag().value()));
            users.add(user);
        }
        int[] insertResult = userDao.insertBatch(users, batchSize);
        System.out.println("Batch insert: " + Arrays.toString(insertResult));
        return IntStream.range(0, users.size())
                .filter(i -> insertResult[i] == 0)
                .mapToObj(users::get)
                .collect(Collectors.toList());
    }

    public List<FailedEmail> processMulti(final InputStream is, int batchSize) throws Exception {
        Map<String, Future<List<String>>> futureMap = new LinkedHashMap<>();
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);
        List<User> usersChunk = new ArrayList<>(batchSize);
        int id = userDao.getSeqAndSkip(batchSize);

        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            final String email = processor.getAttribute("email");
            final UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
            final String fullName = processor.getReader().getElementText();
            final User user = new User(id++, fullName, email, flag);
            usersChunk.add(user);
            if (usersChunk.size() == batchSize) {
                addFuture(futureMap, usersChunk);
                usersChunk = new ArrayList<>(batchSize);
                id = userDao.getSeqAndSkip(batchSize);
            }
        }
        if (!usersChunk.isEmpty()) {
            addFuture(futureMap, usersChunk);
        }

        List<FailedEmail> failedUsers = new ArrayList<>();
        List<String> allAlreadyPresents = new ArrayList<>();
        futureMap.forEach((range, future) -> {
            try {
                List<String> alreadyPresentInChunk = future.get();
                log.info("{} successfully executed with already presents: {}", range, alreadyPresentInChunk);
                allAlreadyPresents.addAll(alreadyPresentInChunk);
            } catch (Exception e) {
                log.error("{} failed", range, e);
                failedUsers.add(new FailedEmail(range, e.toString()));
            }
        });

        if (!allAlreadyPresents.isEmpty()) {
            failedUsers.add(new FailedEmail(allAlreadyPresents.toString(), "already presents"));
        }
        return failedUsers;
    }

    private void addFuture(Map<String, Future<List<String>>> futureMap, List<User> chunk) {
        String emails = chunk.get(0).getEmail();
        if (chunk.size() > 1) {
            emails += '-' + chunk.get(chunk.size() - 1).getEmail();
        }
        Future<List<String>> future = executorService.submit(() -> userDao.insertAndGetConflict(chunk));
        futureMap.put(emails, future);
        log.info("Submit chunk: {}", emails);
    }


    public static class FailedEmail {
        public String emailOrRange;
        public String cause;

        public FailedEmail(String emailOrRange, String cause) {
            this.emailOrRange = emailOrRange;
            this.cause = cause;
        }

        @Override
        public String toString() {
            return "BatchResult{" +
                    "emailOrRange='" + emailOrRange + '\'' +
                    ", cause='" + cause + '\'' +
                    '}';
        }
    }
}
