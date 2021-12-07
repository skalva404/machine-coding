package kalva.mc.cron;

import kalva.mc.cron.db.CronStoreMemoryImpl;
import kalva.mc.cron.model.Job;
import kalva.mc.cron.model.JobInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TestCronStore {

    @Test
    void withOneTime() {

        CronStoreMemoryImpl store = new CronStoreMemoryImpl();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MINUTE, 5);
        store.save(new Job(UUID.randomUUID().toString(),
                Schedule.withOneTime(date.getTime()),
                System.out::println));

        Queue<JobInstance> jobInstances = store.getInstances(5);
        Assertions.assertEquals(1, jobInstances.size());
    }

    @Test
    void withDelayed() {

        CronStoreMemoryImpl store = new CronStoreMemoryImpl();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MINUTE, 5);
        store.save(new Job(UUID.randomUUID().toString(),
                Schedule.withDelayed(TimeUnit.SECONDS.toMillis(1)),
                System.out::println));

        Queue<JobInstance> jobInstances = store.getInstances(5);
        Assertions.assertEquals(1, jobInstances.size());

        jobInstances = store.getAndMaksSuccess(5, TimeUnit.SECONDS);
        Assertions.assertEquals(0, jobInstances.size());
    }

    @Test
    void withRecurring() {

        CronStoreMemoryImpl store = new CronStoreMemoryImpl();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MINUTE, 5);
        store.save(new Job(UUID.randomUUID().toString(),
                Schedule.withRecurring(date.getTime(), TimeUnit.SECONDS.toMillis(10), 2L),
                System.out::println));

        Queue<JobInstance> jobInstances = store.getInstances(5);
        Assertions.assertEquals(2, jobInstances.size());
    }

    @Test
    void withRecurringAndTime() throws InterruptedException {

        CronStoreMemoryImpl store = new CronStoreMemoryImpl();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.SECOND, 0);
        store.save(new Job(UUID.randomUUID().toString(),
                Schedule.withRecurring(date.getTime(), TimeUnit.SECONDS.toMillis(2), 2L),
                System.out::println));

        Queue<JobInstance> jobInstances = store.getAndMaksSuccess(5, TimeUnit.MILLISECONDS);
        Assertions.assertEquals(1, jobInstances.size());

        TimeUnit.SECONDS.sleep(3);
        jobInstances = store.getAndMaksSuccess(5, TimeUnit.MILLISECONDS);
        Assertions.assertEquals(1, jobInstances.size());
    }

    @Test
    void withRecurringAndTime_1() {

        CronStoreMemoryImpl store = new CronStoreMemoryImpl();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.SECOND, 0);
        store.save(new Job(UUID.randomUUID().toString(),
                Schedule.withRecurring(date.getTime(), TimeUnit.SECONDS.toMillis(2), 2L),
                System.out::println));

        Queue<JobInstance> jobInstances = store.getAndMaksSuccess(5, TimeUnit.SECONDS);
        Assertions.assertEquals(2, jobInstances.size());
    }
}
