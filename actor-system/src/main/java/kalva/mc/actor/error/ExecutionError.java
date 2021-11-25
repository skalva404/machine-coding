package kalva.mc.actor.error;

public class ExecutionError extends RuntimeException {
    public ExecutionError(Exception e) {
        super(e);
    }

    public ExecutionError(Throwable e) {
        super(e);
    }
}
