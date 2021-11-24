package kalva.mc.cron;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestTaskSchedulerService {

    @Test
    void simpleTest() throws InterruptedException {
        TaskSchedulerService service = TaskSchedulerService.getService();
        service.start();
        service.schedule(Schedule.withOneTime(new Date()), context -> {
            log.info("executing the task {}", context.toString());
        });
        TimeUnit.SECONDS.sleep(1);
        service.shutdown();
    }
}
