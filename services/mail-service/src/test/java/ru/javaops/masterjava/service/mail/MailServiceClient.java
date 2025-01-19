package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import jakarta.activation.DataHandler;
import jakarta.xml.ws.Service;
import ru.javaops.masterjava.web.WebStateException;

import javax.xml.namespace.QName;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class MailServiceClient {

    public static void main(String[] args) throws MalformedURLException, WebStateException, URISyntaxException {
        Service service = Service.create(
                new URL("http://localhost:8080/mail/mailService?wsdl"),
                new QName("http://mail.javaops.ru/", "MailServiceImplService"));
        DataHandler dataHandler = new DataHandler(new File("config_templates/version.html").toURI().toURL());
        Attachment attachment = new Attachment(dataHandler, "version.html");
        MailService mailService = service.getPort(MailService.class);
        String state = mailService.sendToGroup(ImmutableSet.of(new Addressee("8441404@gmail.com", null)), null,
                "Group mail subject", "Group mail body", ImmutableList.of(attachment));
        System.out.println("Group mail state: " + state);

//        GroupResult groupResult = mailService.sendBulk(ImmutableSet.of(
//                new Addressee("Мастер Java <masterjava@javaops.ru>"),
//                new Addressee("Bad Email <bad_email.ru>")), "Bulk mail subject", "Bulk mail body", null);
//        System.out.println("\nBulk mail groupResult:\n" + groupResult);
    }
}
