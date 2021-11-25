package kalva.mc.actor.impl;

import kalva.mc.actor.*;
import kalva.mc.actor.error.ActorNotFound;
import kalva.mc.actor.error.ResourceNotInitialized;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class SimpleActorService implements ActorService {

    private final Config config;
    private Map<String, Listener> listeners;
    private volatile boolean initialized = false;
    private AtomicBoolean shutdown = new AtomicBoolean(Boolean.FALSE);

    public SimpleActorService(Config config) {
        this.config = config;
    }

    public void init() {
        log.info("starting simple actionservice");
        initialized = true;
        listeners = new ConcurrentHashMap<>();
        shutdown = new AtomicBoolean(Boolean.FALSE);
    }

    private void preCheck() {
        if (!initialized) {
            throw new ResourceNotInitialized();
        }
    }

    public void subscribe(Actor actor) {
        preCheck();
        listeners.putIfAbsent(actor.actorId(), new SimpleListener(actor, new SimpleMailBox(config.mailBoxSize())));
        log.info("subscribed the new actor {}", actor.actorId());
    }

    public void unSubscribe(String refId) throws Exception {
        preCheck();
        Listener listener = listeners.get(refId);
        if (null != listener) {
            listener.close();
            listeners.remove(refId);
            log.info("unsubscribed the  actor {}", refId);
        }
    }

    public void publishMessage(String refId, Message message) throws ActorNotFound {
        preCheck();
        Listener listener = listeners.get(refId);
        if (null != listener) {
            listener.trigger(message);
            log.debug("published the message to {}", refId);
        } else {
            throw new ActorNotFound(refId);
        }
    }

    public synchronized void close() {
        if (shutdown.get()) {
            return;
        }
        initialized = false;
        log.info("stopping simple actionservice");
        shutdown.set(Boolean.FALSE);
        listeners.forEach((id, listener) -> {
            log.info("stopping the listener {}", id);
            try {
                listener.close();
            } catch (Exception e) {
                log.info("error in closing actor service", e);
            }
        });
    }
}
