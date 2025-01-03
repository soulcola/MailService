package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.UserGroup;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class UserGroupDao implements AbstractDao {

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE user_group CASCADE ")
    @Override
    public abstract void clean();

    @SqlBatch("INSERT INTO user_group (user_id, group_id) VALUES (:userId, :froupId)" +
            "ON CONFLICT DO NOTHING")
    public abstract void insertBatch(@BindBean List<UserGroup> userGroups);

    @SqlQuery("SELECT user_id FROM user_group WHERE group_id=:it")
    public abstract Set<Integer> getUserIds(@Bind int groupId);

    public static List<UserGroup> toUserGroups(int userId, Integer... groupIds) {
        return Arrays.stream(groupIds).map(groupId -> new UserGroup(groupId, userId)).collect(Collectors.toList());
    }

    public static Set<Integer> getByGroupId(int groupId, List<UserGroup> userGroups) {
        return userGroups.stream()
                .filter(group -> group.getGroupId() == groupId)
                .map(UserGroup::getUserId)
                .collect(Collectors.toSet());
    }
}
