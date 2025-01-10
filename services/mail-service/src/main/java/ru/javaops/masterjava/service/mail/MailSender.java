package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.mail2.core.EmailException;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.dao.MailDao;
import ru.javaops.masterjava.service.model.Mail;

import java.util.Set;

@Slf4j
public class MailSender {

    private static final MailDao mailDao = DBIProvider.getDao(MailDao.class);

    static void sendMail(Set<Addressee> to, Set<Addressee> cc, String subject, String body) {
        log.info("Send mail to '{}' cc '{}' subject '{}{}", to, cc, subject, log.isDebugEnabled() ? "\nbody=" + body : "");
        String status = "OK";
        try {
            val email = MailConfig.createHtmlEmail();
            email.setSubject(subject);
            email.setMsg(body);
            for (Addressee addressee : to) {
                email.addTo(addressee.getEmail());
            }
            for (Addressee addressee : cc) {
                email.addCc(addressee.getEmail());
            }
            log.info("Send mail {}", email);

            //  https://yandex.ru/blog/company/66296
            email.setHeaders(ImmutableMap.of("List-Unsubscribe", "<mailto:8441404@mail.ru.ru?subject=Unsubscribe&body=Unsubscribe>"));

            email.send();
        } catch (EmailException e) {
            log.error(e.getMessage(), e);
            status = e.getMessage();
        }
        mailDao.insert(Mail.of(to, cc, subject, status));
    }
}
