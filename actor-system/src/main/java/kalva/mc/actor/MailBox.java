package kalva.mc.actor;

import java.util.concurrent.TimeUnit;

public interface MailBox {
    void put(Message message);

    Message get() throws InterruptedException;

    Message get(long timeout, TimeUnit unit) throws InterruptedException;

    Integer ramaining();
}
