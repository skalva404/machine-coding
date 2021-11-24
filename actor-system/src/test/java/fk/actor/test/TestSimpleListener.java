package fk.actor.test;

import fk.actor.Actor;
import fk.actor.Listener;
import fk.actor.Message;
import fk.actor.impl.SimpleListener;
import fk.actor.impl.SimpleMailBox;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestSimpleListener {

    @Test
    public void exceptionTest() throws InterruptedException {
        Listener listener = new SimpleListener(
                new Actor() {
                    @Override
                    public void onMessage(Message message) {
                        throw new RuntimeException("bla bla");
                    }

                    @Override
                    public String actorId() {
                        return "UnKnown";
                    }
                },
                new SimpleMailBox(10));
        listener.init();
        listener.notifyMessage(new Message("Testing".getBytes()));
        listener.shutdown();
    }

    @Test
    public void simpleTest() throws InterruptedException {
        DummyActor dummyActor = new DummyActor();
        SimpleMailBox simpleMailBox = new SimpleMailBox(10);
        Listener listener = new SimpleListener(dummyActor, simpleMailBox);
        listener.init();
        for (int i = 1; i <= 5; i++) {
            listener.notifyMessage(new Message("Testing".getBytes()));
        }
        TimeUnit.SECONDS.sleep(1);
        listener.shutdown();
        Assert.assertEquals(5, dummyActor.counter);
    }

    @Test(expected = IllegalStateException.class)
    public void shutdownErrorTest() {
        DummyActor dummyActor = new DummyActor();
        SimpleMailBox simpleMailBox = new SimpleMailBox(1);
        Listener listener = new SimpleListener(dummyActor, simpleMailBox);
        listener.init();
        listener.notifyMessage(new Message("Testing".getBytes()));
        listener.shutdown();
        listener.notifyMessage(new Message("Testing".getBytes()));
    }

    class DummyActor implements Actor {
        int counter = 0;

        @Override
        public void onMessage(Message message) {
            counter++;
            log.info("executing test task {} -- {}", new String(message.data()), counter);
        }

        @Override
        public String actorId() {
            return "DummyActor";
        }
    }
}
