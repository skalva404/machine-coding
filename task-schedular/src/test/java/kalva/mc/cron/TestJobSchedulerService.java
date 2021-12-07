package kalva.mc.cron;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestJobSchedulerService {

    public Map<String, String> paramMap = Map.of("p1-key", "p1-value",
            "p2-key", "p2-value");

    @Test
    void withOneTime() throws InterruptedException {

        TaskScheduler service = TaskScheduler.getService();
        service.start();

        Shared.reset();
        service.schedule(DummyTask.class.getCanonicalName(), paramMap, Schedule.withOneTime(new Date()));

        TimeUnit.SECONDS.sleep(1);
        service.shutdown();

        Assertions.assertTrue(Shared.executed.get(0).get());
    }

    @Test
    void withDelayed() throws InterruptedException {
        TaskScheduler service = TaskScheduler.getService();
        service.start();

        Shared.reset();
        service.schedule(DummyTask.class.getCanonicalName(), paramMap, Schedule.withDelayed(TimeUnit.SECONDS.toMillis(5)));

        TimeUnit.SECONDS.sleep(7);
        service.shutdown();

        Assertions.assertTrue(Shared.executed.get(0).get());
    }

    @Test
    void withRecurring() throws InterruptedException {
        TaskScheduler service = TaskScheduler.getService();
        service.start();

        int count = 2;
        Shared.reset();
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 5);
        service.schedule(DummyTask.class.getCanonicalName(), paramMap,
                Schedule.withRecurring(instance.getTime(), TimeUnit.SECONDS.toMillis(5), 2L));

        TimeUnit.SECONDS.sleep(5);
        for (int i = 0; i < count; i++) {
            Assertions.assertTrue(Shared.executed.get(i).get());
        }

        TimeUnit.SECONDS.sleep(5);
        service.shutdown();
    }
}
