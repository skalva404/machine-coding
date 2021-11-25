package kalva.mc.cron;

import kalva.mc.cron.db.CronStore;
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

        CronStore store = new CronStore();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MINUTE, 5);
        store.save(new Job(UUID.randomUUID().toString(),
                Schedule.withOneTime(date.getTime()),
                context -> System.out.println(context.toString())));

        Queue<JobInstance> jobInstances = store.getJobInstances(5);
        Assertions.assertEquals(1, jobInstances.size());
    }

    @Test
    void withDelayed() {

        CronStore store = new CronStore();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MINUTE, 5);
        store.save(new Job(UUID.randomUUID().toString(),
                Schedule.withDelayed(TimeUnit.SECONDS.toMillis(1)),
                context -> System.out.println(context.toString())));

        Queue<JobInstance> jobInstances = store.getJobInstances(5);
        Assertions.assertEquals(1, jobInstances.size());

        jobInstances = store.getInstancesForNext(5, TimeUnit.SECONDS);
        Assertions.assertEquals(0, jobInstances.size());
    }

    @Test
    void withRecurring() {

        CronStore store = new CronStore();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MINUTE, 5);
        store.save(new Job(UUID.randomUUID().toString(),
                Schedule.withRecurring(date.getTime(), TimeUnit.SECONDS.toMillis(10), 2),
                context -> System.out.println(context.toString())));

        Queue<JobInstance> jobInstances = store.getJobInstances(5);
        Assertions.assertEquals(2, jobInstances.size());
    }

    @Test
    void withRecurringAndTime() throws InterruptedException {

        CronStore store = new CronStore();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.SECOND, 0);
        store.save(new Job(UUID.randomUUID().toString(),
                Schedule.withRecurring(date.getTime(), TimeUnit.SECONDS.toMillis(2), 2),
                context -> System.out.println(context.toString())));

        Queue<JobInstance> jobInstances = store.getInstancesForNext(5, TimeUnit.MILLISECONDS);
        Assertions.assertEquals(1, jobInstances.size());

        TimeUnit.SECONDS.sleep(3);
        jobInstances = store.getInstancesForNext(5, TimeUnit.MILLISECONDS);
        Assertions.assertEquals(1, jobInstances.size());
    }

    @Test
    void withRecurringAndTime_1() {

        CronStore store = new CronStore();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.SECOND, 0);
        store.save(new Job(UUID.randomUUID().toString(),
                Schedule.withRecurring(date.getTime(), TimeUnit.SECONDS.toMillis(2), 2),
                context -> System.out.println(context.toString())));

        Queue<JobInstance> jobInstances = store.getInstancesForNext(5, TimeUnit.SECONDS);
        Assertions.assertEquals(2, jobInstances.size());
    }
}
