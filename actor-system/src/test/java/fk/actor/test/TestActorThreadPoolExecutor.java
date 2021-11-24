package fk.actor.test;

import fk.actor.error.ResourceNotAvailable;
import fk.actor.impl.ActorThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TestActorThreadPoolExecutor {

    @Test
    public void succesfullExecutionTest() throws InterruptedException {
        AtomicInteger success = new AtomicInteger();
        ActorThreadPoolExecutor poolExecutor = new ActorThreadPoolExecutor(10, 1);
        for (int i = 1; i <= 10; i++) {
            poolExecutor.submit(() -> {
                success.incrementAndGet();
                log.info("executing test task {}", success.get());
            });
        }
        poolExecutor.shutdown();
        Assert.assertEquals(10, success.get());
    }

    @Test(expected = ResourceNotAvailable.class)
    public void rejectionHandlerTest() {
        ActorThreadPoolExecutor poolExecutor = new ActorThreadPoolExecutor(1, 1);
        for (int i = 1; i <= 10; i++) {
            poolExecutor.execute(() -> {//Do Nothing
            });
        }
        poolExecutor.shutdown();
    }
}
