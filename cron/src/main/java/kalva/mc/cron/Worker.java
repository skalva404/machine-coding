package kalva.mc.cron;

import kalva.mc.cron.db.CronStore;
import kalva.mc.cron.model.JobInstance;
import kalva.mc.cron.q.TaskQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class Worker extends Thread implements Service {

    protected CronStore store;
    protected TaskQueue<JobInstance> queue;
    protected AtomicBoolean stop = new AtomicBoolean(false);

    public Worker(String name, CronStore store, TaskQueue<JobInstance> queue) {
        super(name);
        this.queue = queue;
        this.store = store;
    }

    public void shutdown() {
        log.info("stopping the {} worker", getName());
        stop.set(true);
    }

    @Override
    public void run() {
        while (!stop.get()) {
            try {
                Status execute = execute();
                if (execute == Status.BACKOFF) {
                    log.warn("Worker is backing off " + getName());
                    TimeUnit.MILLISECONDS.sleep(500);
                }
            } catch (Exception e) {
                log.error("ignoring the errors for now ", e);
            }
        }
    }

    public abstract Status execute();
}