package ru.javaops.masterjava.persist;

import ru.javaops.masterjava.persist.dao.UserGroupDao;
import ru.javaops.masterjava.persist.model.UserGroup;

import java.util.List;
import java.util.Set;

import static ru.javaops.masterjava.persist.GroupTestData.*;
import static ru.javaops.masterjava.persist.UserTestData.*;
import static ru.javaops.masterjava.persist.dao.UserGroupDao.toUserGroups;

public class UserGroupTestData {
    public static List<UserGroup> USER_GROUPS;


    public static void init() {
        UserTestData.init();
        UserTestData.setUp();

        GroupTestData.init();
        GroupTestData.setUp();

        USER_GROUPS = toUserGroups(ADMIN.getId(), TOPJAVA_07.getId(), TOPJAVA_08.getId(), MASTERJAVA_01.getId());
        USER_GROUPS.addAll(toUserGroups(FULL_NAME.getId(), TOPJAVA_07.getId(), MASTERJAVA_01.getId()));
        USER_GROUPS.addAll(toUserGroups(USER1.getId(), TOPJAVA_06.getId(), MASTERJAVA_01.getId()));
        USER_GROUPS.add(new UserGroup(USER2.getId(), MASTERJAVA_01.getId()));
        USER_GROUPS.add(new UserGroup(USER3.getId(), MASTERJAVA_01.getId()));

    }

    public static void setUp() {
        UserGroupDao dao = DBIProvider.getDao(UserGroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            dao.insertBatch(USER_GROUPS);
        });
    }

    public static Set<Integer> getByGroupId(int groupId) {
        return UserGroupDao.getByGroupId(groupId, USER_GROUPS);
    }
}
