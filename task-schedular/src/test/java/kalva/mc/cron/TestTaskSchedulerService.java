package kalva.mc.cron;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class TestTaskSchedulerService {

    @Test
    void name() {
    }

    @Test
    void simpleTest() throws InterruptedException {
        AtomicBoolean executed = new AtomicBoolean(false);
        TaskSchedulerService service = TaskSchedulerService.getService();
        service.start();
        service.schedule(Schedule.withOneTime(new Date()),
                context -> {
                    executed.set(true);
                    log.info("executing the task {}", context.toString());
                });
        TimeUnit.SECONDS.sleep(1);
        service.shutdown();
        Assertions.assertTrue(executed.get());
    }
}
