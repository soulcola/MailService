package ru.javaops.masterjava.service.mail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javaops.masterjava.persist.dao.AbstractDaoTest;
import ru.javaops.masterjava.service.dao.MailDao;

import static ru.javaops.masterjava.service.mail.MailTestData.DATE_FROM;
import static ru.javaops.masterjava.service.mail.MailTestData.MAILS;

public class MailDaoTest extends AbstractDaoTest<MailDao> {
    public MailDaoTest() {
        super(MailDao.class);
    }
    @Before
    public void setUp() {
        MailTestData.setUp();
    }

    @Test
    public void getAfter() {
        Assert.assertEquals(MAILS, dao.getAfter(DATE_FROM));
    }

}
