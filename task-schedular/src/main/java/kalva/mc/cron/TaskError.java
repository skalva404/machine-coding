package kalva.mc.cron;

public class TaskError extends Exception {

    public TaskError() {
        super();
    }

    public TaskError(String message) {
        super(message);
    }

    public TaskError(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskError(Throwable cause) {
        super(cause);
    }
}
