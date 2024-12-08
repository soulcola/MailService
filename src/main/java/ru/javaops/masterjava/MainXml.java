package ru.javaops.masterjava;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: gkislin
 * Date: 05.08.2015
 *
 * @link http://caloriesmng.herokuapp.com/
 * @link https://github.com/JavaOPs/topjava
 */
public class MainXml {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public static void main(String[] args) throws IOException, JAXBException {
        String projectName = args[0];
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());

        Project project = payload.getProjects().getProject().stream()
                .filter(project1 -> project1.getName().equals(projectName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("wrong project name"));
        payload.getUsers().getUser().stream()
                .filter(user -> user.getGroups().removeAll(project.getGroup()))
                .sorted(Comparator.comparing(User::getValue))
                .collect(Collectors.toList())
                .forEach(user -> System.out.println(user.getValue()));
    }
}
