package kalva.mc.cron;

import kalva.mc.cron.model.Job;
import kalva.mc.cron.model.JobInstance;

import java.util.Map;
import java.util.Queue;

public interface TaskStore {

    void save(Job job);

    void save(JobInstance jobInstance);

    Map<String, String> getParameterMap(String jobId);

    Queue<JobInstance> getTaskInstancesForNext(long timeInMillis);

}
