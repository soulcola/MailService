package ru.javaops.masterjava.service.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.activation.DataHandler;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlMimeType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Attachment {
    // http://stackoverflow.com/questions/12250423/jax-ws-datahandler-getname-is-blank-when-called-from-client-side
    protected String name;

    @XmlMimeType("application/octet-stream")
    private DataHandler dataHandler;
}
