package kalva.mc.cron;

import kalva.mc.cron.db.CronStoreMemoryImpl;
import kalva.mc.cron.model.JobInstance;
import kalva.mc.cron.q.TaskQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TaskRunner implements Service {

    private final TaskStore store;
    private final Integer threadCount;
    private ExecutorService executorService;

    private Worker producer;
    private Worker consumer;

    public TaskRunner(final TaskStore store,
                      final Integer threadCount) {
        this.store = store;
        this.threadCount = threadCount;
    }

    @Override
    public void start() {
        log.info("starting the TaskRunner");
        TaskQueue<JobInstance> queue = new TaskQueue<>();
        executorService = new TaskPoolExecutor(threadCount, 50);
//                ThreadPoolExecutor(threadCount,
//                threadCount,
//                0L,
//                TimeUnit.MILLISECONDS,
//                new ArrayBlockingQueue<>(50),
//                (r, executor) -> log.warn("task is rejected {}", r.toString()));

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
        while (!executorService.isTerminated()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ignore) {
            }
            log.info("executorService still executing few jobs, waiting for them to finish");
        }
    }

    private static class Producer extends Worker {

        public Producer(String name, TaskStore store, TaskQueue<JobInstance> queue) {
            super(name, store, queue);
        }

        @Override
        public Status execute() {

            if (stop.get()) {
                log.info("asked to stop the worker, stopping it");
                stopped.set(true);
                return Status.SUCCESS;
            }
            log.info("getting next 1 min jobs");
//            if (queue.getCurrentCount() > 0) {
//                log.warn("there are jobs still running from the previous run {}", queue.getCurrentCount());
//                if (FairnessStrategy.CLEAR.equals(strategy)) {
//                    queue.clear();
//                    log.info("selected strategy is CLEAR, so all jobs in queue ");
//                }
//            }
            Queue<JobInstance> instancesForNext = ((CronStoreMemoryImpl) store).getAndMaksSuccess(1, TimeUnit.MINUTES);
            if (0 == instancesForNext.size()) {
                return Status.BACKOFF;
            }
            for (JobInstance next : instancesForNext) {
                log.info("sending to queue {}", next.taskId());
                boolean send = queue.send(next);
                while (!send) {
                    log.warn("queue is full, waiting for empty space");
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                        send = queue.send(next);
                    } catch (InterruptedException ignore) {
                        send = true;
                    }
                }
            }
            return Status.SUCCESS;
        }
    }

    public class Consumer extends Worker {

        public Consumer(String name, TaskStore store, TaskQueue<JobInstance> queue) {
            super(name, store, queue);
        }

        @Override
        public Status execute() {

            if (stop.get()) {
                if (0 < queue.getCurrentCount()) {
                    log.warn("issued a stop signal, but we have tasks to complete {} ", queue.getCurrentCount());
                } else {
                    log.info("asked to stop the worker, stopping it");
                    stopped.set(true);
                    return Status.SUCCESS;
                }
            }

            log.info("consuming jobs ...");
            JobInstance instance = queue.consume();
            if (null == instance) {
                return Status.BACKOFF;
            }
            log.info("consumed job {}", instance.taskId());
            try {
                executorService.submit(() -> {
                    try {
                        Map<String, String> parameterMap = store.getParameterMap(instance.taskId());
                        instance.task().execute(new TaskContext(instance.taskId(), parameterMap));
                    } catch (Throwable e) {
                        log.error("failed job {} and the execution time {}", instance.taskId(), instance.executionTime(), e);
                    }
                });
            } catch (RejectedExecutionException ree) {
                //TODO handle this error
            }
            return Status.SUCCESS;
        }
    }
}
