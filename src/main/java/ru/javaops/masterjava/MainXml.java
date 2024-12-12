package ru.javaops.masterjava;

import com.google.common.io.Resources;
import j2html.TagCreator;
import j2html.tags.specialized.BodyTag;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.Project;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static j2html.TagCreator.td;
import static j2html.TagCreator.tr;

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

        Set<User> users = payload.getUsers().getUser().stream()
                .filter(user -> user.getGroups().removeAll(project.getGroup()))
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(User::getValue).thenComparing(User::getEmail))));

        BodyTag body = TagCreator.body(
                TagCreator.table(),
                tr(),
                TagCreator.th("Name"),
                TagCreator.th("Email"),
                TagCreator.each(users, user -> tr(
                                td(user.getValue()),
                                td(user.getEmail())
                        )
                )
        );
        System.out.println(body.render());
    }
}
