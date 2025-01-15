package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import jakarta.annotation.Resource;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.persist.DBITestProvider;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import jakarta.xml.ws.Endpoint;
import java.io.File;
import java.util.List;

public class MailServicePublisher {

    public static void main(String[] args) {
        DBITestProvider.initDBI();

        Endpoint endpoint = Endpoint.create(new MailServiceImpl());
        File wsdlFile = Configs.getConfigFile("wsdl/mailService.wsdl");
        List<Source> metadata = ImmutableList.of(new StreamSource(wsdlFile));

        endpoint.setMetadata(metadata);
        endpoint.publish("http://localhost:8080/mail/mailService");
    }
}
