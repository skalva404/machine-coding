package kalva.mc.actor;

public interface Actor {
    void onMessage(Message message);

    String actorId();
}
