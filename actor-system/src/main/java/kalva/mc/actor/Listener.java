package kalva.mc.actor;

public interface Listener extends LifeCycle {

    void trigger(Message message);
}
