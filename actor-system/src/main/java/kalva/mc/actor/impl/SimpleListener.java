package kalva.mc.actor.impl;

import kalva.mc.actor.*;
import kalva.mc.actor.error.ExecutionError;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleListener extends TaskRunner implements Listener {

    private Actor actor;
    private MailBox mailBox;

    public SimpleListener(Actor actor, MailBox mailBox) {
        super(actor.actorId(), mailBox);
        this.actor = actor;
        this.mailBox = mailBox;
    }

    @Override
    public void notifyMessage(Message message) {
        if (shutdown) {
            throw new IllegalStateException("actor " + actor.actorId() + " is not live");
        }
        mailBox.put(message);
    }

    @Override
    public void processMessage(Message message) {
        try {
            ActorSystem.resourcePool().submit(() -> actor.onMessage(message)).get();
        } catch (Throwable e) {
            throw new ExecutionError(e);
        }
    }
}
