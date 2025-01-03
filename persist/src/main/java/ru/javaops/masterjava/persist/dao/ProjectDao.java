package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class ProjectDao implements AbstractDao {

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE project CASCADE ")
    @Override
    public abstract void clean();

    public void insert(Project project) {
        int id = insertGeneratedId(project);
        project.setId(id);
    }

    @SqlQuery("SELECT * FROM project ORDER BY name")
    public abstract List<Project> getAll();

    @SqlUpdate("INSERT INTO project (name, description) VALUES (:name, :description) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Project project);

    public Map<String, Project> getAsMap() {
        return getAll().stream().collect(Collectors.toMap(Project::getName, p -> p));
    }

}
