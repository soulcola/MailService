package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableMap;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.GroupType;

import java.util.Map;

import static ru.javaops.masterjava.persist.ProjectTestData.MASTERJAVA_ID;
import static ru.javaops.masterjava.persist.ProjectTestData.TOPJAVA_ID;

public class GroupTestData {
    public static Group TOPJAVA_06;
    public static Group TOPJAVA_07;
    public static Group TOPJAVA_08;
    public static Group MASTERJAVA_01;
    public static Map<String, Group> GROUPS;

    public static void init() {
        ProjectTestData.setUp();
        TOPJAVA_06 = new Group("topjava06", GroupType.FINISHED, TOPJAVA_ID);
        TOPJAVA_07 = new Group("topjava07", GroupType.FINISHED, TOPJAVA_ID);
        TOPJAVA_08 = new Group("topjava08", GroupType.CURRENT, TOPJAVA_ID);
        MASTERJAVA_01 = new Group("masterjava01", GroupType.CURRENT, MASTERJAVA_ID);
        GROUPS = ImmutableMap.of(
                TOPJAVA_06.getName(), TOPJAVA_06,
                TOPJAVA_07.getName(), TOPJAVA_07,
                TOPJAVA_08.getName(), TOPJAVA_08,
                MASTERJAVA_01.getName(), MASTERJAVA_01
        );
    }

    public static void setUp() {
        GroupDao dao = DBIProvider.getDao(GroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> GROUPS.values().forEach(dao::insert));
    }
}
