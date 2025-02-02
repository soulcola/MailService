package ru.javaops.masterjava.service.mail.rest;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("rest")
public class MailRestConfig extends ResourceConfig {
    public MailRestConfig() {
        packages("ru.javaops.masterjava.service.mail");
    }
}
