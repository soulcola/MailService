package ru.javaops.masterjava.webapp;

import com.google.common.collect.ImmutableList;
import com.sun.xml.ws.encoding.DataHandlerDataSource;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimePart;
import jakarta.mail.internet.MimePartDataSource;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.service.mail.Attachment;
import ru.javaops.masterjava.service.mail.GroupResult;
import ru.javaops.masterjava.service.mail.MailWSClient;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;

@WebServlet("/send")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10) //10 MB in memory limit
@Slf4j
public class SendServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result;
        try {
            log.info("Start sending");
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            String users = req.getParameter("users");
            String subject = req.getParameter("subject");
            String body = req.getParameter("body");
            Part filePart = req.getPart("fileToUpload");
            DataSource dataSource = new ByteArrayDataSource(filePart.getInputStream(), "application/octet-stream");
            DataHandler dataHandler = new DataHandler(dataSource);
            GroupResult groupResult = MailWSClient.sendBulk(MailWSClient.split(users), subject, body, ImmutableList.of(new Attachment(dataHandler, filePart.getSubmittedFileName())));
            result = groupResult.toString();
            log.info("Processing finished with result: {}", result);
        } catch (Exception e) {
            log.error("Processing failed", e);
            result = e.toString();
        }
        resp.getWriter().write(result);
    }
}
