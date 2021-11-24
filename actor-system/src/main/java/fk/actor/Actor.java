package fk.actor;

public interface Actor {
    void onMessage(Message message);

    String actorId();
}
