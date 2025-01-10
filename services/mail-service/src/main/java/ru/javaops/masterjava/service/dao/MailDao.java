package ru.javaops.masterjava.service.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.dao.AbstractDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.service.model.Mail;

import java.util.Date;
import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MailDao implements AbstractDao {

    @SqlUpdate("TRUNCATE mail_hist CASCADE ")
    @Override
    public abstract void clean();

    @SqlQuery("SELECT * FROM mail_hist")
    public abstract List<Mail> getAll();

    @SqlUpdate("INSERT INTO mail_hist (send_to, copy_to, subject, datetime, status) " +
            "VALUES (:to, :cc, :subject, :dateTime, :status)")
    @GetGeneratedKeys
    public abstract int insert(@BindBean Mail email);

    @SqlQuery("SELECT * FROM mail_hist where datetime>=:after ORDER BY datetime DESC")
    public abstract List<Mail> getAfter(@Bind("after") Date after);
}
