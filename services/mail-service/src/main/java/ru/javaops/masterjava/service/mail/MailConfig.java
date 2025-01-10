package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail2.core.EmailException;
import org.apache.commons.mail2.javax.DefaultAuthenticator;
import org.apache.commons.mail2.javax.Email;
import org.apache.commons.mail2.javax.HtmlEmail;
import ru.javaops.masterjava.config.Configs;

import javax.mail.Authenticator;
import java.nio.charset.StandardCharsets;

@ToString
@Slf4j
public class MailConfig {
    private static final MailConfig INSTANCE =
            new MailConfig(Configs.getConfig("mail.conf", "mail"));

    private final String host;
    private final int port;
    private final String username;
    private final boolean useSSL;
    private final boolean useTLS;
    private final boolean debug;
    private final Authenticator authenticator;
    private final String fromName;

    private MailConfig(Config config) {
        this.host = config.getString("host");
        this.port = config.getInt("port");
        this.username = config.getString("username");
        this.authenticator = new DefaultAuthenticator(username, config.getString("password"));
        this.useSSL = config.getBoolean("useSSL");
        this.useTLS = config.getBoolean("useTLS");
        this.debug = config.getBoolean("debug");
        this.fromName = config.getString("fromName");
    }

    public <T extends Email> T prepareEmail(T email) throws EmailException {
        email.setFrom(username, fromName);
        email.setHostName(host);
        if (useSSL){
            email.setSslSmtpPort(String.valueOf(port));
        } else {
            email.setSmtpPort(port);
        }
        email.setAuthenticator(authenticator);
        email.setSSLOnConnect(useSSL);
        email.setStartTLSEnabled(useTLS);
        email.setDebug(debug);
        email.setCharset(StandardCharsets.UTF_8.name());
        return email;
    }

    public static HtmlEmail createHtmlEmail() throws EmailException {
        log.info("Config: {}", INSTANCE);
        return INSTANCE.prepareEmail(new HtmlEmail());
    }
}
