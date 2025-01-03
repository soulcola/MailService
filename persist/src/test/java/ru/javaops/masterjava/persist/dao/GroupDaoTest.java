package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.GroupTestData;

import static ru.javaops.masterjava.persist.GroupTestData.GROUPS;

public class GroupDaoTest extends AbstractDaoTest<GroupDao> {

    public GroupDaoTest() {
        super(GroupDao.class);
    }

    @BeforeClass
    public static void init() {
        GroupTestData.init();
    }

    @Before
    public void setUp() {
        GroupTestData.setUp();
    }

    @Test
    public void getAll() {
        Assert.assertEquals(dao.getAsMap(), GROUPS);
        System.out.println(GROUPS.values());
    }

}
