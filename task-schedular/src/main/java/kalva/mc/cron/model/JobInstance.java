package kalva.mc.cron.model;

import kalva.mc.cron.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class JobInstance {

    private String taskId;
    private Task task;
    private Date executionTime;
}
