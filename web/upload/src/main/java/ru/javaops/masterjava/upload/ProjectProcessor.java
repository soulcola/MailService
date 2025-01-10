package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class ProjectProcessor {
    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);
    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
    private final GroupProcessor groupProcessor = new GroupProcessor();
    public Map<String, Group> process(StaxStreamProcessor processor) throws XMLStreamException {
        val map = projectDao.getAsMap();

        while (processor.startElement("Project", "Projects")) {
            val name = processor.getAttribute("name");
            if (!map.containsKey(name)) {
                Project project = new Project(name, processor.getElementValue("description"));
                log.info("Insert: {}", project);
                projectDao.insert(project);
                groupProcessor.process(processor, project.getId());
            }
        }
        return groupDao.getAsMap();
    }
}
