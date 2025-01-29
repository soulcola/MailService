package ru.javaops.masterjava.service.mail.handlers;

import com.typesafe.config.Config;
import org.slf4j.event.Level;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.web.handler.SoapLoggingHandlers;

public abstract class MailServiceLoggingHandlers extends SoapLoggingHandlers {
    private static Config HOSTS;
    public static String CLIENT_LEVEL;
    public static String SERVER_LEVEL;

    static {
        HOSTS = Configs.getConfig("hosts.conf", "hosts").getConfig("mail");
        CLIENT_LEVEL = HOSTS.getString("debug.client");
        SERVER_LEVEL = HOSTS.getString("debug.server");
    }

    protected MailServiceLoggingHandlers(Level loggingLevel) {
        super(loggingLevel);
    }



    public static class ClientHandler extends MailServiceLoggingHandlers {
        public ClientHandler() {
            super(Level.valueOf(CLIENT_LEVEL));
        }

        @Override
        protected boolean isRequest(boolean isOutbound) {
            return isOutbound;
        }
    }

    public static class ServerHandler extends MailServiceLoggingHandlers {

        public ServerHandler() {
            super(Level.valueOf(SERVER_LEVEL));
        }

        @Override
        protected boolean isRequest(boolean isOutbound) {
            return !isOutbound;
        }
    }
}
