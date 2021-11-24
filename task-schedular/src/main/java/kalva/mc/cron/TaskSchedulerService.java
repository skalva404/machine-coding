package kalva.mc.cron;

import kalva.mc.cron.db.CronStore;
import kalva.mc.cron.model.Job;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public final class TaskSchedulerService implements Service {

    private final CronStore store = new CronStore();
    private final TaskRunner runner = new TaskRunner(store, 100);
    private final static TaskSchedulerService singleton;

    static {
        singleton = new TaskSchedulerService();
    }

    private TaskSchedulerService() {
    }

    public static TaskSchedulerService getService() {
        return singleton;
    }

    public String schedule(Schedule schedule, Task task) {
        String uuid = UUID.randomUUID().toString();
        Job job = new Job(uuid, schedule, task);
        store.save(job);
        return uuid;
    }

    @Override
    public void start() {
        log.info("starting TaskSchedulerService");
        runner.start();
    }

    @Override
    public void shutdown() {
        log.info("stopping TaskSchedulerService");
        runner.shutdown();
    }
}
