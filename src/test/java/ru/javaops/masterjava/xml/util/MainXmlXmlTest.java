package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.junit.Test;
import ru.javaops.masterjava.xml.schema.Group;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class MainXmlXmlTest {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    @Test
    public void mainTest() throws IOException, JAXBException {
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());

        List<Group> groups = payload.getProjects().getProject().stream()
                .filter(project1 -> project1.getName().equals("topjava"))
                .findFirst()
                .get()
                .getGroup();

        payload.getUsers().getUser().stream()
                .filter(user -> !Collections.disjoint(user.getGroups(), groups))
                .collect(Collectors.toList()).forEach(user -> System.out.println(user.getEmail()));

    }
}
