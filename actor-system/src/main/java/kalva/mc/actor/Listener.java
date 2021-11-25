package kalva.mc.actor;

public interface Listener extends LifeCycle {

    void notifyMessage(Message message);
}
