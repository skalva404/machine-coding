package kalva.mc.cron;

import java.util.Map;

public record TaskContext(String taskId, Map<String, String> paramMap) {

    public String taskId() {
        return taskId;
    }
}
