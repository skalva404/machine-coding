package fk.actor.error;

public class ResourceNotAvailable extends RuntimeException {
    public ResourceNotAvailable(String message) {
        super(message);
    }
}
