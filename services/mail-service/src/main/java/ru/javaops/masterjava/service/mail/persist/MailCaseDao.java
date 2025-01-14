package ru.javaops.masterjava.service.mail.persist;


import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.javaops.masterjava.persist.dao.AbstractDao;

import java.util.Date;
import java.util.List;

public interface MailCaseDao extends AbstractDao {

    @SqlUpdate("TRUNCATE mail_hist")
    @Override
    void clean();

    @SqlQuery("SELECT * FROM mail_hist WHERE datetime >= :after ORDER BY datetime DESC")
    @RegisterFieldMapper(MailCase.class)
    List<MailCase> getAfter(@Bind("after") Date date);

    @SqlUpdate("INSERT INTO mail_hist (list_to, list_cc, subject, state, datetime)  VALUES (:listTo, :listCc, :subject, :state, :datetime)")
    @GetGeneratedKeys
    int insert(@BindBean MailCase mails);
}
