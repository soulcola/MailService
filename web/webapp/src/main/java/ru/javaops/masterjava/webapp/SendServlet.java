package ru.javaops.masterjava.webapp;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.service.mail.Addressee;
import ru.javaops.masterjava.service.mail.GroupResult;
import ru.javaops.masterjava.service.mail.MailWSClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@WebServlet("/send")
public class SendServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] usersString = req.getParameterValues("users[]");
        Set<Addressee> users = Arrays.stream(usersString).map(Addressee::new).collect(Collectors.toSet());
        String subject = req.getParameter("subject");
        String body = req.getParameter("body");
        log.info("body: {}, subject: {}, to: {}", body, subject, users);
        GroupResult result = MailWSClient.sendBulk(users, subject, body);
        resp.getWriter().println(result.toString());
    }
}
