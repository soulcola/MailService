package ru.javaops.masterjava.persist.dao;

import one.util.streamex.StreamEx;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.javaops.masterjava.persist.model.UserGroup;

import java.util.List;
import java.util.Set;

public interface UserGroupDao extends AbstractDao {

    @SqlUpdate("TRUNCATE user_group CASCADE")
    @Override
    void clean();

    @SqlBatch("INSERT INTO user_group (user_id, group_id) VALUES (:userId, :groupId)")
    void insertBatch(@BindBean List<UserGroup> userGroups);

    @SqlQuery("SELECT user_id FROM user_group WHERE group_id=:groupId")
    @RegisterBeanMapper(UserGroup.class)
    Set<Integer> getUserIds(@Bind("groupId") int groupId);

    static List<UserGroup> toUserGroups(int userId, Integer... groupIds) {
        return StreamEx.of(groupIds).map(groupId -> new UserGroup(userId, groupId)).toList();
    }

    static Set<Integer> getByGroupId(int groupId, List<UserGroup> userGroups) {
        return StreamEx.of(userGroups).filter(ug -> ug.getGroupId() == groupId).map(UserGroup::getUserId).toSet();
    }
}