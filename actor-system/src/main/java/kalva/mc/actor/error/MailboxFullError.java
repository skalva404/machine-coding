package kalva.mc.actor.error;

public class MailboxFullError extends RuntimeException {
    public MailboxFullError(String message) {
        super(message);
    }
}
