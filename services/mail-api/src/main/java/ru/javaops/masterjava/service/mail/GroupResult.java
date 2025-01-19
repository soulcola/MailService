package ru.javaops.masterjava.service.mail;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class GroupResult {
    private int success; // number of successfully sent email
    private List<MailResult> failed; // failed emails with causes

    @Override
    public String toString() {
        return "Success: " + success + '\n' +
                (failed == null ? "" : "Failed: " + failed.toString());
    }
}