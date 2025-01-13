package ru.javaops.masterjava.persist.dao;

import one.util.streamex.StreamEx;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;
import java.util.Map;

public interface ProjectDao extends AbstractDao {

    @SqlUpdate("TRUNCATE project CASCADE ")
    @Override
    void clean();

    @SqlQuery("SELECT * FROM project ORDER BY name")
    @RegisterBeanMapper(Project.class)
    List<Project> getAll();

    default Map<String, Project> getAsMap() {
        return StreamEx.of(getAll()).toMap(Project::getName, g -> g);
    }

    @SqlUpdate("INSERT INTO project (name, description)  VALUES (:name, :description)")
    @GetGeneratedKeys
    int insertGeneratedId(@BindBean Project project);

    default void insert(Project project) {
        int id = insertGeneratedId(project);
        project.setId(id);
    }
}
