package kalva.mc.actor.error;

public class ActorNotFound extends Throwable {
    public ActorNotFound(String refId) {
        super(refId);
    }
}
