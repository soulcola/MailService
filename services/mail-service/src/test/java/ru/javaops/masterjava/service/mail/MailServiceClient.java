package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.URL;

public class MailServiceClient {


    public static void main(String[] args) throws IOException {
        Service service = Service.create(
                new URL("http://localhost:8080/mail/mailService?wsdl"),
                new QName("http://mail.service.masterjava.javaops.ru/", "MailServiceImplService"));

        MailService mailService = service.getPort(MailService.class);
        mailService.sendMail(ImmutableSet.of(
                new Addressee("8441404@gmail.com"),
                new Addressee("Petr <fake-EMAIL.com>")), ImmutableSet.of(), "Subject", "Body");

        mailService.sendMail(ImmutableSet.of(
                new Addressee("Petr <8441404@gmail.com>")), ImmutableSet.of(), "Subject", "Body");
    }
}
