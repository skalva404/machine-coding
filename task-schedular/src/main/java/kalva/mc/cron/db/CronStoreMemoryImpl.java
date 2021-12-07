package kalva.mc.cron.db;

import kalva.mc.cron.TaskStore;
import kalva.mc.cron.model.Job;
import kalva.mc.cron.model.JobInstance;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class CronStoreMemoryImpl implements TaskStore {

    //Ideally all these maps will be a different tables when we use db based design
    private final Map<String, Job> registry = new HashMap<>();
    private final Map<String, Map<String, String>> jobParameters = new HashMap<>();
    private final PriorityQueue<JobInstance> priorityQueue = new PriorityQueue<>(Comparator.comparing(JobInstance::executionTime));

    public synchronized void save(Job job) {
        log.info("storing the task for time {} with id {}", job.schedule().startDate(), job.id());
        registry.put(job.id(), job);
        //In db based approach, we will serialize the param key to db.
        jobParameters.put(job.id(), job.paramMap());
        save(new JobInstance(job.id(), job.task(), job.schedule().startDate()));
    }

    public synchronized void save(JobInstance jobInstance) {
        log.info("storing the taskInstance for time {} with id {}", jobInstance.executionTime(), jobInstance.taskId());
        priorityQueue.offer(jobInstance);
    }

    @Override
    public Map<String, String> getParameterMap(String jobId) {
        return jobParameters.get(jobId);
    }

    public synchronized Queue<JobInstance> getAndMaksSuccess(int time, TimeUnit unit) {
        long maxAllowed = System.currentTimeMillis() + unit.toMillis(time);
        return getTaskInstancesForNext(maxAllowed);
    }

    /**
     * This function is returning the task instances for the given time interval
     * Also it is marking the success for the given tasks.
     *
     * @param timeInMillis
     * @return Queue<TaskInstance>
     */
    public synchronized Queue<JobInstance> getTaskInstancesForNext(long timeInMillis) {
        Queue<JobInstance> instances = new ArrayDeque<>();
        JobInstance peek = priorityQueue.peek();
        while (null != peek) {
            if (peek.executionTime().getTime() <= timeInMillis) {
                log.info("retrieving the task id {}", peek.taskId());
                instances.offer(priorityQueue.poll());
                markSuccess(peek.taskId());
            } else {
                return instances;
            }
            peek = priorityQueue.peek();
        }
        return instances;
    }

    public synchronized Job markSuccess(String taskId) {

        Job job = registry.get(taskId);
        if (null == job) {
            log.warn("no task found for {}", taskId);
            return job;
        }
        Long remaining = job.remaining();
        remaining--;
        if (remaining == 0) {
            registry.remove(taskId);
            log.info("completed all occurances of {}", taskId);
            return job;
        }

        log.info("remaining occurances of {} are {}", taskId, remaining);
        Job newJob = new Job(taskId, job.schedule(), job.task());
        newJob.updateRemaining(remaining);
        registry.put(taskId, newJob);

        long nextExecTime = System.currentTimeMillis() + job.schedule().repeatInMills();
        Date nextSchedule = new Date(nextExecTime);
        log.info("next scheduled time for task {} is {}", taskId, nextSchedule);
        save(new JobInstance(job.id(), job.task(), nextSchedule));
        return newJob;
    }

    public synchronized Queue<JobInstance> getInstances(int count) {
        Queue<JobInstance> instances = new ArrayDeque<>(count);
        for (int i = 1; i <= count; i++) {
            JobInstance poll = priorityQueue.poll();
            if (null != poll) {
                log.info("retrieving the job id {}", poll.taskId());
                instances.offer(poll);
                markSuccess(poll.taskId());
            }
        }
        return instances;
    }
}
