package ru.javaops.masterjava.persist.dao;

import one.util.streamex.StreamEx;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.javaops.masterjava.persist.model.City;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;

public interface CityDao extends AbstractDao {

    @SqlUpdate("TRUNCATE city CASCADE ")
    @Override
    void clean();

    @SqlQuery("SELECT * FROM city")
    @RegisterFieldMapper(City.class)
    List<City> getAll();

    default Map<String, City> getAsMap() {
        return StreamEx.of(getAll()).toMap(City::getRef, identity());
    }

    @SqlUpdate("INSERT INTO city (ref, name) VALUES (:ref, :name)")
    void insert(@BindBean City city);

    @SqlBatch("INSERT INTO city (ref, name) VALUES (:ref, :name)")
    void insertBatch(@BindBean Collection<City> cities);
}
