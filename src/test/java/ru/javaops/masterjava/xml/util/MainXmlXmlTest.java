package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import j2html.Config;
import j2html.TagCreator;
import j2html.tags.specialized.BodyTag;
import org.junit.Test;
import ru.javaops.masterjava.xml.schema.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static j2html.TagCreator.td;
import static j2html.TagCreator.tr;


public class MainXmlXmlTest {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    @Test
    public void mainTest() throws IOException, JAXBException {
        String projectName = "topjava";
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
                TagCreator.table(
                        tr(
                                TagCreator.th("Name"),
                                TagCreator.th("Email")
                        ),
                        TagCreator.each(users, user -> tr(
                                        td(user.getValue()),
                                        td(user.getEmail())
                                )
                        )
                )
        );
        System.out.println(body.render());

    }
}
