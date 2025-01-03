package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableMap;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.Map;

public class CityTestData {
    public static City MOW = new City("mow", "Москва");
    public static City SPB = new City("spb", "Санкт-Петербург");
    public static City KIV = new City("kiv", "Киев");
    public static City MNSK = new City("mnsk", "Минск");
    public static Map<String, City> CITIES = ImmutableMap.of(
            KIV.getRef(), KIV,
            MNSK.getRef(), MNSK,
            SPB.getRef(), SPB,
            MOW.getRef(), MOW
    );

    public static void setUp() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            CITIES.values().forEach(dao::insert);
        });
    }
}
