package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javaops.masterjava.persist.ProjectTestData;

import static ru.javaops.masterjava.persist.ProjectTestData.PROJECTS;

public class ProjectDaoTest extends AbstractDaoTest<ProjectDao> {

    public ProjectDaoTest() {
        super(ProjectDao.class);
    }

    @Before
    public void setUp() {
        ProjectTestData.setUp();
    }

    @Test
    public void getAll() {
        Assert.assertEquals(dao.getAsMap(), PROJECTS);
        System.out.println(PROJECTS.values());
    }

}
