package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class CityProcessor {

    private static final CityDao cityDao = DBIProvider.getDao(CityDao.class);

    public Map<String, City> process(final StaxStreamProcessor processor) throws XMLStreamException {
        log.info("Start city processing");
        Map<String, City> cities = cityDao.getAsMap();
        List<City> newCities = new ArrayList<>();

        while (processor.startElement("City", "Cities")) {
            String ref = processor.getAttribute("id");
            String name = processor.getReader().getElementText();
            if (!cities.containsKey(ref)) {
                newCities.add(new City(ref, name));
            }
        }
        log.info("Insert batch {}", newCities);
        cityDao.insertBatch(newCities);
        return cityDao.getAsMap();
    }
}
