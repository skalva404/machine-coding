package kalva.mc.actor;

import kalva.mc.actor.error.ActorNotFound;

public interface ActorService extends LifeCycle {

    void subscribe(Actor actor);

    void unSubscribe(String refId) throws Exception;

    void publishMessage(String refId, Message message) throws ActorNotFound;
}
