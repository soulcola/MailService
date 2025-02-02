package ru.javaops.masterjava.service.mail.rest;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import ru.javaops.masterjava.service.mail.GroupResult;
import ru.javaops.masterjava.service.mail.MailServiceExecutor;
import ru.javaops.masterjava.service.mail.MailWSClient;
import ru.javaops.masterjava.web.WebStateException;

import java.util.Collections;

@Path("/")
public class MailRS {

    @GET
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    public String test() {
        return "test";
    }

    public GroupResult send(@NotNull @FormParam("users") String users,
                            @NotNull @FormParam("subject") String subject,
                            @NotNull @FormParam("body") String body) throws WebStateException {
        return MailServiceExecutor.sendBulk(MailWSClient.split(users), subject, body, Collections.EMPTY_LIST);
    }
}
