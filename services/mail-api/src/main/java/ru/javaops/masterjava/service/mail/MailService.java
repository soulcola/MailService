package ru.javaops.masterjava.service.mail;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * gkislin
 * 15.11.2016
 */
@WebService
public interface MailService {

    @WebMethod
    void sendMail(
            @WebParam(name = "to") Set<Addressee> to,
            @WebParam(name = "cc") Set<Addressee> cc,
            @WebParam(name = "subject") String subject,
            @WebParam(name = "body") String body) throws IOException;
}