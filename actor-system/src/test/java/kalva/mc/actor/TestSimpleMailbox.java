package kalva.mc.actor;

import kalva.mc.actor.error.MailboxFullError;
import kalva.mc.actor.impl.SimpleMailBox;
import org.junit.Assert;
import org.junit.Test;

public class TestSimpleMailbox {

    @Test
    public void testPutnGet() throws InterruptedException {
        String message = "testing";
        MailBox mailBox = new SimpleMailBox(5);
        mailBox.put(new Message(message.getBytes()));
        Assert.assertEquals(message, new String(mailBox.get().data()));
    }

    @Test(expected = MailboxFullError.class)
    public void testSizeFull() {
        MailBox mailBox = new SimpleMailBox(5);
        for (int i = 1; i <= 5; i++) {
            mailBox.put(new Message("testing".getBytes()));
        }
        mailBox.put(new Message("testing".getBytes()));
    }
}
