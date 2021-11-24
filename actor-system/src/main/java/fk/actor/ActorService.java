package fk.actor;

import fk.actor.error.ActorNotFound;

public interface ActorService extends LifeCycle {

    void subscribe(Actor actor);

    void unSubscribe(String refId);

    void publishMessage(String refId, Message message) throws ActorNotFound;
}
