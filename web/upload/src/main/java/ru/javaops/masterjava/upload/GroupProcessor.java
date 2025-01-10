package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.persist.model.type.GroupType;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.Map;

@Slf4j
public class GroupProcessor {
    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);

    public Map<String, Group> process(StaxStreamProcessor processor, int projectId) throws XMLStreamException {
        val map = groupDao.getAsMap();

        while (processor.startElement("Group", "Project")) {
            val name = processor.getAttribute("name");
            if (!map.containsKey(name)) {
                Group group = new Group(name, GroupType.valueOf(processor.getAttribute("type")), projectId);
                log.info("Insert: {}", group);
                groupDao.insert(group);
            }
        }
        return groupDao.getAsMap();
    }
}
