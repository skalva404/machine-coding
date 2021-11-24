package kalva.mc.cron.db;

import kalva.mc.cron.model.Job;
import kalva.mc.cron.model.JobInstance;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class CronStore {

    private final Map<String, Job> registry = new HashMap<>();
    private final PriorityQueue<JobInstance> priorityQueue = new PriorityQueue<>(Comparator.comparing(JobInstance::executionTime));

    public synchronized void save(Job job) {
        log.info("storing the job for time {} with id {}", job.schedule().startDate(), job.id());
        registry.put(job.id(), job);
        schedule(new JobInstance(job.id(), job.task(), job.schedule().startDate()));
    }

    public synchronized void schedule(JobInstance jobInstance) {
        priorityQueue.offer(jobInstance);
    }

    public synchronized Queue<JobInstance> getInstancesForNext(int time, TimeUnit unit) {
        long maxAllowed = System.currentTimeMillis() + unit.toMillis(time);
        Queue<JobInstance> instances = new ArrayDeque<>();
        while (true) {
            JobInstance peek = priorityQueue.peek();
            if (null == peek) {
                break;
            }
            if (peek.executionTime().getTime() <= maxAllowed) {
                log.info("retrieving the job id {}", peek.jobId());
                instances.offer(priorityQueue.poll());
                markSuccess(peek.jobId());
            } else {
                break;
            }
        }
        return instances;
    }

    public synchronized Queue<JobInstance> getJobInstances(int count) {
        Queue<JobInstance> instances = new ArrayDeque<>(count);
        for (int i = 1; i <= count; i++) {
            JobInstance poll = priorityQueue.poll();
            if (null != poll) {
                log.info("retrieving the job id {}", poll.jobId());
                instances.offer(poll);
                markSuccess(poll.jobId());
            }
        }
        return instances;
    }

    public synchronized Job markSuccess(String jobId) {
        Job job = registry.get(jobId);
        if (null == job) {
            log.warn("no job found for {}", jobId);
            return job;
        }
        Integer remaining = job.remaining();
        remaining--;
        if (remaining == 0) {
            registry.remove(jobId);
            log.info("completed all occurances of {}", jobId);
            return job;
        }

        log.info("remaining occurances of {} are {}", jobId, remaining);
        Job newJob = new Job(jobId, job.schedule(), job.task());
        newJob.updateRemaining(remaining);
        registry.put(jobId, newJob);

        long nextExecTime = System.currentTimeMillis() + job.schedule().repeatInMills();
        Date nextSchedule = new Date(nextExecTime);
        log.info("next scheduled time for job {} is {}", jobId, nextSchedule);
        schedule(new JobInstance(job.id(), job.task(), nextSchedule));
        return newJob;
    }
}
