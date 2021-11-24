package kalva.mc.cron.model;

import kalva.mc.cron.Schedule;
import kalva.mc.cron.Task;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Job {

    private String id;
    private Schedule schedule;
    private Task task;
    private Integer remaining;

    public Job(String id, Schedule schedule, Task task) {
        this.id = id;
        this.schedule = schedule;
        this.task = task;
        updateRemaining(schedule.numberOfTime());
    }

    public Job updateRemaining(Integer remaining) {
        this.remaining = remaining;
        return this;
    }
}
