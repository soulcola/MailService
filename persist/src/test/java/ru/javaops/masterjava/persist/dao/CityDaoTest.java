package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javaops.masterjava.persist.CityTestData;

import static ru.javaops.masterjava.persist.CityTestData.CITIES;

public class CityDaoTest extends AbstractDaoTest<CityDao> {

    public CityDaoTest() {
        super(CityDao.class);
    }

    @Before
    public void setUp() {
        CityTestData.setUp();
    }

    @Test
    public void getAll() {
        Assert.assertEquals(dao.getAsMap(), CITIES);
        System.out.println(CITIES.values());
    }

}
