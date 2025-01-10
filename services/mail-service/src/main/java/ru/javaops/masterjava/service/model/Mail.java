package ru.javaops.masterjava.service.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import com.google.common.base.Joiner;
import lombok.*;
import ru.javaops.masterjava.persist.model.BaseEntity;
import ru.javaops.masterjava.service.mail.Addressee;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Mail extends BaseEntity {

    private @Column("send_to") String to;
    private @Column("copy_to") String cc;
    private String subject;
    private String status;
    private Date dateTime;

    public static Mail of(Set<Addressee> to,
                          Set<Addressee> cc,
                          String subject,
                          String status) {
        return new Mail(
                Joiner.on(", ").join(to),
                Joiner.on(", ").join(cc),
                subject,
                status, new Date());
    }
}
