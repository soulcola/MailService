package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.GroupTestData;

import java.util.Set;

import static ru.javaops.masterjava.persist.GroupTestData.*;

public class UserGroupDaoTest extends AbstractDaoTest<UserGroupDao> {

    public UserGroupDaoTest() {
        super(UserGroupDao.class);
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
        Set<Integer> userIds = dao.getUserIds(MASTERJAVA_01.getId());
        Assert.assertEquals(dao.getUserIds(MASTERJAVA_01.getId()), userIds);

        userIds = dao.getUserIds(TOPJAVA_07.getId());
        Assert.assertEquals(dao.getUserIds(TOPJAVA_07.getId()), userIds);
    }
}
