package kalva.mc.actor;

public interface Actor {
    void onMessage(Message message);

    String actorId();

    default Type type() {
        return Type.ASYNC;
    }

    public static enum Type {
        SYNC, ASYNC
    }
}
