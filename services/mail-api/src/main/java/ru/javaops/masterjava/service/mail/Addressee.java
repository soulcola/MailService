package ru.javaops.masterjava.service.mail;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "email")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class Addressee {
    @XmlAttribute
    private @NonNull String email;
    @XmlValue
    private String name;

    public Addressee(String email) {
        email = email.trim();
        int idx = email.indexOf('<');
        if (idx == -1) {
            this.email = email;
        } else {
            this.name = email.substring(0, idx).trim();
            this.email = email.substring(idx + 1, email.length() - 1).trim();
        }
    }

    @Override
    public String toString() {
        return name == null ? email : name + " <" + email + '>';
    }
}
