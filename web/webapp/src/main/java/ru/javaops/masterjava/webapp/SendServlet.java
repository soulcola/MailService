package ru.javaops.masterjava.webapp;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.service.mail.Addressee;
import ru.javaops.masterjava.service.mail.MailServiceExecutor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/send")
public class SendServlet extends HttpServlet {
    private UserDao userDao = DBIProvider.getDao(UserDao.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        Set<Addressee> to, String subject, String body
        Set<Addressee> to = Arrays.stream(req.getParameterValues("to"))
                .map(Addressee::new)
                .collect(Collectors.toSet());
        String subject = req.getParameter("subject");
        String body = req.getParameter("body");
        MailServiceExecutor.sendBulk(to, subject, body);

    }
}
