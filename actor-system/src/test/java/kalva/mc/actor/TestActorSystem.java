package kalva.mc.actor;

import kalva.mc.actor.error.ActorNotFound;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class TestActorSystem {

    @Test
    public void basicTest() throws ActorNotFound, Exception {

        try (ActorService actorService = ActorSystem.actorService()) {

            final boolean[] executed = {false};
            actorService.init();
            final int[] counter = {0};
            actorService.subscribe(new Actor() {
                @Override
                public void onMessage(Message message) {
                    counter[0]++;
                    log.info("executing test task {}", counter[0]);
                    executed[0] = true;
                }

                @Override
                public String actorId() {
                    return "Actor";
                }
            });
            actorService.publishMessage("Actor", new Message("Testing".getBytes()));
            actorService.close();
            Assert.assertEquals(1, counter[0]);
            Assert.assertTrue(executed[0]);
        }
    }
}
