package ru.javaops.masterjava.service.mail;

import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.web.WsClient;

import javax.xml.namespace.QName;
import java.util.Set;

@Slf4j
public class MailWSClient {
    private static final WsClient<MailService> WS_CLIENT;

    static {
        WS_CLIENT = new WsClient<>(Resources.getResource("wsdl/mailService.wsdl"),
                new QName("http://mail.javaops.ru/", "MailServiceImplService"),
                MailService.class);

        WS_CLIENT.init("mail", "/mail/mailService?wsdl");
    }


    public static String sendToGroup(final Set<Addressee> to, final Set<Addressee> cc, final String subject, final String body) {
        log.info("Send mail to '" + to + "' cc '" + cc + "' subject '" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        try{
            return WS_CLIENT.getPort().sendToGroup(to, cc, subject, body);
        } catch (Exception e){
            return e.getMessage();
        }

    }

    public static GroupResult sendBulk(final Set<Addressee> to, final String subject, final String body) {
        log.info("Send mail to '{}' subject '{}{}", to, subject, log.isDebugEnabled() ? "\nbody=" + body : "");
        GroupResult result;
        try{
            result = WS_CLIENT.getPort().sendBulk(to, subject, body);
        } catch (Exception e){
            result = new GroupResult(e);
        }
        log.info("Send mail to {} with result: {}", to, result);
        return result;
    }
}
