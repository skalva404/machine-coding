package fk.actor.test;

import fk.actor.*;
import fk.actor.error.ActorNotFound;
import fk.actor.error.ResourceNotInitialized;
import fk.actor.impl.SimpleActorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestActorService {

    @Test
    public void subscribeTest() throws ActorNotFound {
        ActorService actorService = new SimpleActorService(new Config().mailBoxSize(1).threadPoolSize(1));
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

    @Test(expected = ActorNotFound.class)
    public void unSubscribeTest() throws InterruptedException, ActorNotFound {
        ActorService actorService = new SimpleActorService(new Config().mailBoxSize(1).threadPoolSize(1));
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
        TimeUnit.SECONDS.sleep(1);
        actorService.unSubscribe("Actor");
        actorService.publishMessage("Actor", new Message("Testing".getBytes()));
        actorService.shutdown();
    }

    @Test(expected = ResourceNotInitialized.class)
    public void actorNotInitializedSubscribeTest() {
        ActorService actorService = new SimpleActorService(new Config().mailBoxSize(1).threadPoolSize(1));
        actorService.subscribe(new Actor() {
            @Override
            public void onMessage(Message message) {
                log.info("executing test task");
            }

            @Override
            public String actorId() {
                return "Actor";
            }
        });
    }

    @Test(expected = ResourceNotInitialized.class)
    public void actorNotInitializedPublishTest() throws ActorNotFound {
        ActorService actorService = new SimpleActorService(new Config().mailBoxSize(1).threadPoolSize(1));
        actorService.publishMessage("123", new Message("Testing".getBytes()));
    }

    @Test(expected = ActorNotFound.class)
    public void actorNotFoundTest() throws ActorNotFound {
        ActorService actorService = new SimpleActorService(new Config().mailBoxSize(1).threadPoolSize(1));
        actorService.init();
        actorService.publishMessage("123", new Message("Testing".getBytes()));
    }
}
