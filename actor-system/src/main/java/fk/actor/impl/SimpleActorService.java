package fk.actor.impl;

import fk.actor.*;
import fk.actor.error.ActorNotFound;
import fk.actor.error.ResourceNotInitialized;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class SimpleActorService implements ActorService {

    private volatile boolean initialized = false;
    private Config config;
    private Map<String, Listener> listeners;
    private AtomicBoolean shutdownCalled = new AtomicBoolean(Boolean.FALSE);

    public SimpleActorService(Config config) {
        this.config = config;
    }

    @Override
    public void subscribe(Actor actor) {
        preCheck();
        listeners.putIfAbsent(actor.actorId(), new SimpleListener(actor, new SimpleMailBox(config.mailBoxSize())));
        log.info("subscribed the new actor {}", actor.actorId());
    }

    @Override
    public void unSubscribe(String refId) {
        preCheck();
        Listener listener = listeners.get(refId);
        if (null != listener) {
            listener.shutdown();
            listeners.remove(refId);
            log.info("unsubscribed the  actor {}", refId);
        }
    }

    @Override
    public void publishMessage(String refId, Message message) throws ActorNotFound {
        preCheck();
        Listener listener = listeners.get(refId);
        if (null != listener) {
            listener.notifyMessage(message);
            log.debug("published the message to {}", refId);
        } else {
            throw new ActorNotFound(refId);
        }
    }

    private void preCheck() {
        if (!initialized) {
            throw new ResourceNotInitialized();
        }
    }

    @Override
    public void init() {
        log.info("starting simple actionservice");
        initialized = true;
        listeners = new ConcurrentHashMap<>();
        shutdownCalled = new AtomicBoolean(Boolean.FALSE);
    }

    @Override
    public synchronized void shutdown() {
        if (shutdownCalled.get()) {
            return;
        }
        initialized = false;
        log.info("stopping simple actionservice");
        shutdownCalled.set(Boolean.FALSE);
        listeners.forEach((id, listener) -> {
            log.info("stopping the listener {}", id);
            listener.shutdown();
        });
    }
}
