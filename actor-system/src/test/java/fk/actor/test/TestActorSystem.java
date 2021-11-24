package fk.actor.test;

import fk.actor.*;
import fk.actor.error.ActorNotFound;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class TestActorSystem {

    @Test
    public void basicTest() throws ActorNotFound {
        ActorService actorService = ActorSystem.actorService();
        actorService.init();
        final int[] counter = {0};
        actorService.subscribe(new Actor() {
            @Override
            public void onMessage(Message message) {
                counter[0]++;
                log.info("executing test task {}", counter[0]);
            }

            @Override
            public String actorId() {
                return "Actor";
            }
        });
        actorService.publishMessage("Actor", new Message("Testing".getBytes()));
        actorService.shutdown();
        Assert.assertEquals(1, counter[0]);
    }
}
