package kalva.mc.cron;

import lombok.experimental.Accessors;

@Accessors(fluent = true)
public record TaskContext(String taskId) {

}
