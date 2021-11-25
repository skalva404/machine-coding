package kalva.mc.cron;

import kalva.mc.cron.db.CronStore;
import kalva.mc.cron.model.JobInstance;
import kalva.mc.cron.q.TaskQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TaskRunner implements Service {

    private final CronStore store;
    private final Integer threadCount;
    private ExecutorService executorService;

    private Worker producer;
    private Worker consumer;

    public TaskRunner(final CronStore store,
                      final Integer threadCount) {
        this.store = store;
        this.threadCount = threadCount;
    }

    @Override
    public void start() {
        log.info("starting the TaskRunner");
        TaskQueue<JobInstance> queue = new TaskQueue<>();
        executorService = new ThreadPoolExecutor(threadCount,
                threadCount,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(50),
                (r, executor) -> log.warn("task is rejected {}", r.toString()));

        producer = new Producer("Task-Producer", store, queue);
        consumer = new Consumer("Task-Consumer", store, queue);

        producer.start();
        consumer.start();
    }

    @Override
    public void shutdown() {
        log.info("stopping the TaskRunner");
        log.info("stopping the producer");
        producer.shutdown();
        log.info("stopping the consumer");
        consumer.shutdown();
        log.info("stopping the executorService");
        executorService.shutdown();
    }

    private class Producer extends Worker {

        public Producer(String name, CronStore store, TaskQueue<JobInstance> queue) {
            super(name, store, queue);
        }

        @Override
        public Status execute() {
            log.info("getting next 1 min jobs");
            Queue<JobInstance> instancesForNext = store.getInstancesForNext(1, TimeUnit.MINUTES);
            if (0 == instancesForNext.size()) {
                return Status.BACKOFF;
            }
            for (JobInstance next : instancesForNext) {
                log.info("sending to queue {}", next.jobId());
                queue.send(next);
            }
            return Status.SUCCESS;
        }
    }

    public class Consumer extends Worker {

        public Consumer(String name, CronStore store, TaskQueue<JobInstance> queue) {
            super(name, store, queue);
        }

        @Override
        public Status execute() {
            log.info("consuming jobs ...");
            JobInstance instance = queue.consume();
            if (null == instance) {
                return Status.BACKOFF;
            }
            log.info("consumed job {}", instance.jobId());
            executorService.submit(() -> {
                try {
                    instance.task().execute(new TaskContext(instance.jobId()));
                } catch (Throwable e) {
                    log.error("failed job {}", instance.jobId(), e);
                }
            });
            return Status.SUCCESS;
        }
    }
}
