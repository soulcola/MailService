package ru.javaops.masterjava.service.mail;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class MailResult {
    public static final String OK = "OK";

    @XmlAttribute
    private @NonNull String email;
    @XmlValue
    private String result;

    public boolean isOk() {
        return OK.equals(result);
    }

    @Override
    public String toString() {
        return "'" + email + "' result: " + result;
    }
}