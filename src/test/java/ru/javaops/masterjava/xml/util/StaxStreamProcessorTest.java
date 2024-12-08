package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.junit.Test;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaxStreamProcessorTest {
    @Test
    public void readCities() throws Exception {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT) {
                    if ("City".equals(reader.getLocalName())) {
                        System.out.println(reader.getElementText());
                    }
                }
            }
        }
    }

    @Test
    public void readCities2() throws Exception {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            String city;
            while ((city = processor.getElementValue("City")) != null) {
                System.out.println(city);
            }
        }
    }

    @Test
    public void readGroupUsers() throws Exception {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            XMLStreamReader reader = processor.getReader();
            String name = "topjava";
            List<String> projectGroups = new ArrayList<>();
            boolean foundProjectName = false;
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT) {
                    String tagName = reader.getLocalName();
                    if (tagName.equals("Project") && name.equals(reader.getAttributeValue(null, "name"))) {
                        foundProjectName = true;
                    }
                    if ("Group".equals(tagName) && foundProjectName) {
                        projectGroups.add(reader.getAttributeValue(null, "name"));
                    }
                    if ("User".equals(tagName)) {
                        List<String> userGroups = new ArrayList<>(Arrays.asList(getAttributeValue(reader, "groups").split(" ")));
                        if (userGroups.removeAll(projectGroups)) {
                            System.out.println(reader.getAttributeValue(null, "email"));
                            System.out.println(reader.getElementText());
                        }
                    }
                }
                if (event == XMLEvent.END_ELEMENT) {
                    String tagName = reader.getLocalName();
                    if (tagName.equals("Project")) {
                        foundProjectName = false;
                    }
                }
            }
        }
    }

    private String getAttributeValue(XMLStreamReader reader, String attributeName) {
        if (reader.getAttributeValue(null, attributeName) != null) {
            return reader.getAttributeValue(null, attributeName);
        }
        return "";
    }
}