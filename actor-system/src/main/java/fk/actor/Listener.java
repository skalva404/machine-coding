package fk.actor;

public interface Listener extends LifeCycle {

    void notifyMessage(Message message);
}
