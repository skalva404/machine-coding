package kalva.mc.cron;

import kalva.mc.cron.q.TaskQueue;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class TestTaskQueue {

    @Test
    void simpleTest() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(2);
        TaskQueue<Task> queue = new TaskQueue<>(2);

        queue.send(context -> System.out.println(context.toString()));
        queue.send(context -> System.out.println(context.toString()));

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (countDownLatch.getCount() > 0) {
                    Task consume = queue.consume();
                    consume.execute(new TaskContext(UUID.randomUUID().toString()));
                    countDownLatch.countDown();
                }
            }
        }).start();
        countDownLatch.await();
    }
}
