package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableSet;
import jakarta.activation.DataHandler;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.web.WebStateException;

@Slf4j
public class MailWSClientMain {
    public static void main(String[] args) throws WebStateException {
        DataHandler dataHandler = new DataHandler("String", "type/text");
        String state = MailWSClient.sendToGroup(
                ImmutableSet.of(new Addressee("To <8441404@gmail.com>")),
                ImmutableSet.of(), "Subject", "Body", null);
        System.out.println(state);
    }
}