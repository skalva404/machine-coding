package kalva.mc.cron;

@FunctionalInterface
public interface Task {

    void execute(TaskContext context);
}
