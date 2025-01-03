package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE city CASCADE ")
    @Override
    public abstract void clean();

    public void insert(City city) {
        int id = insertGeneratedId(city);
        city.setId(id);
    }

    @SqlQuery("SELECT * FROM city ORDER BY name")
    public abstract List<City> getAll();

    public Map<String, City> getAsMap (){
        return getAll().stream().collect(Collectors.toMap(City::getRef, city -> city));
    }

    @SqlUpdate("INSERT INTO city (ref, name) VALUES (:ref, :name) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean City city);


    @SqlBatch("INSERT INTO city (ref, name) VALUES (:ref, :name)" +
            "ON CONFLICT DO NOTHING")
    public abstract void insertBatch(@BindBean List<City> users);
}
