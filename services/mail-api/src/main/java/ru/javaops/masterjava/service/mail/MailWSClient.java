package ru.javaops.masterjava.service.mail;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.io.Resources;
import com.typesafe.config.Config;
import jakarta.xml.ws.soap.MTOMFeature;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.web.AuthUtil;
import ru.javaops.masterjava.web.WebStateException;
import ru.javaops.masterjava.web.WsClient;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Set;

@Slf4j
public class MailWSClient {
    private static Config HOSTS;
    private static final WsClient<MailService> WS_CLIENT;
    public static String USER;
    public static String PASSWORD;

    static {
        WS_CLIENT = new WsClient<>(Resources.getResource("wsdl/mailService.wsdl"),
                new QName("http://mail.javaops.ru/", "MailServiceImplService"),
                MailService.class);

        WS_CLIENT.init("endpoint", "/mail/mailService?wsdl");
        HOSTS = Configs.getConfig("hosts.conf", "hosts").getConfig("mail");
        USER = HOSTS.getString("user");
        PASSWORD = HOSTS.getString("password");

    }


    public static String sendToGroup(final Set<Addressee> to, final Set<Addressee> cc, final String subject, final String body, List<Attachment> attachments) throws WebStateException {
        log.info("Send to group to '" + to + "' cc '" + cc + "' subject '" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        String status = getPort().sendToGroup(to, cc, subject, body, attachments);
        log.info("Send to group with status: " + status);
        return status;
    }

    public static GroupResult sendBulk(final Set<Addressee> to, final String subject, final String body, List<Attachment> attachments) throws WebStateException {
        log.info("Send bulk to '" + to + "' subject '" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        GroupResult result = getPort().sendBulk(to, subject, body, attachments);
        log.info("Sent bulk with result: " + result);
        return result;
    }

    private static MailService getPort() {
        MailService port = WS_CLIENT.getPort(new MTOMFeature(1024));
        WsClient.setAuth(port, USER, PASSWORD);
        return port;
    }

    public static Set<Addressee> split(String addressees) {
        Iterable<String> split = Splitter.on(',').trimResults().omitEmptyStrings().split(addressees);
        return ImmutableSet.copyOf(Iterables.transform(split, Addressee::new));
    }
}
