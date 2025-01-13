package ru.javaops.masterjava.persist.dao;

import one.util.streamex.StreamEx;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.javaops.masterjava.persist.model.Group;

import java.util.List;
import java.util.Map;

public interface GroupDao extends AbstractDao {

    @SqlUpdate("TRUNCATE groups CASCADE ")
    @Override
    void clean();

    @SqlQuery("SELECT * FROM groups ORDER BY name")
    @RegisterBeanMapper(Group.class)
    List<Group> getAll();

    default Map<String, Group> getAsMap() {
        return StreamEx.of(getAll()).toMap(Group::getName, g -> g);
    }

    @SqlUpdate("INSERT INTO groups (name, type, project_id)  VALUES (:name, CAST(:type AS group_type), :projectId)")
    @GetGeneratedKeys
    int insertGeneratedId(@BindBean Group groups);

    default void insert(Group groups) {
        int id = insertGeneratedId(groups);
        groups.setId(id);
    }
}
