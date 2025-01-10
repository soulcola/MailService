package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.dao.MailDao;
import ru.javaops.masterjava.service.model.Mail;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class MailTestData {
    private static final Instant now = Instant.now();
    static final Date DATE_FROM = Date.from(now.minus(Duration.ofDays(1)));

    static final List<Mail> MAILS = ImmutableList.of(
            Mail.of(
                    ImmutableSet.of(
                            new Addressee("ИмяTo1 Фамилия1 <mailTo1@ya.ru>"),
                            new Addressee("Имя2 Фамилия2 <mailTo2@ya.ru>")),
                    ImmutableSet.of(
                            new Addressee("ИмяCc1 Фамилия1 <mail1Cc@ya.ru>"),
                            new Addressee("ИмяCc2 Фамилия2 <mailCc2@ya.ru>")),
                    "subject1", "state1"
            ),
            new Mail("toMail2@ya.ru", null, "subject2", "state2",
                    Date.from(now.minus(Duration.ofMinutes(1)))),
            new Mail(null, "ccMail3@ya.ru", "subject3", "state3", DATE_FROM)
    );

    private static final Mail MAILS_EXCLUDED =
            new Mail("toMail4@ya.ru", "ccMail4@ya.ru", "subject4", "state4",
                    Date.from(now.minus(Duration.ofDays(2))));

    public static void setUp() {
        MailDao dao = DBIProvider.getDao(MailDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            MAILS.forEach(dao::insert);
            dao.insert(MAILS_EXCLUDED);
        });
    }
}
