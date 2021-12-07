package kalva.mc.cron.model;

import kalva.mc.cron.Schedule;
import kalva.mc.cron.Task;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Accessors(fluent = true)
public class Job {

    private String id;
    private Schedule schedule;
    private String taskFQN;
    private Long remaining;
    private Map<String, String> paramMap;

    private Task task;

    public Job(String id, Schedule schedule, String taskFQN, Map<String, String> paramMap) {
        this.id = id;
        this.schedule = schedule;
        this.taskFQN = taskFQN;
        this.paramMap = paramMap;
        this.task = taskInstance();
        updateRemaining(schedule.numberOfTime());
    }

    public Job(String id, Schedule schedule, String taskFQN) {
        this.id = id;
        this.schedule = schedule;
        this.taskFQN = taskFQN;
        this.task = taskInstance();
        updateRemaining(schedule.numberOfTime());
    }

    public Job(String id, Schedule schedule, Task task) {

        this.id = id;
        this.schedule = schedule;
        this.taskFQN = task.getClass().getCanonicalName();
        this.task = task;
        updateRemaining(schedule.numberOfTime());
    }

    public Job updateRemaining(Long remaining) {
        this.remaining = remaining;
        return this;
    }

    public Task taskInstance() {
        try {
            Class<?> aClass = Class.forName(taskFQN);
            task = (Task) aClass.getConstructor().newInstance();
            return task;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
