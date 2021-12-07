package kalva.mc.cron;

import kalva.mc.cron.db.CronStoreMemoryImpl;
import kalva.mc.cron.model.Job;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;

@Slf4j
public final class TaskScheduler implements Service {

    private final TaskStore store = new CronStoreMemoryImpl();
    private final TaskRunner runner = new TaskRunner(store, 100);

    private final static TaskScheduler singleton;

    static {
        singleton = new TaskScheduler();
    }

    private TaskScheduler() {
    }

    public static TaskScheduler getService() {
        return singleton;
    }

    public void schedule(String taskFQN, Schedule schedule) {
        schedule(taskFQN, null, schedule);
    }

    public void schedule(String taskFQN, Map<String, String> paramMap, Schedule schedule) {
        Job job = new Job(UUID.randomUUID().toString(), schedule, taskFQN, paramMap);
        store.save(job);
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
