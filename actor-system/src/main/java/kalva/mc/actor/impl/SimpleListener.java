package kalva.mc.actor.impl;

import kalva.mc.actor.*;
import kalva.mc.actor.error.ExecutionError;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.Future;

import static kalva.mc.actor.Actor.Type.SYNC;

@Slf4j
public class SimpleListener extends TaskRunner implements Listener {

    private final Actor actor;
    private final MailBox mailBox;

    public SimpleListener(Actor actor, MailBox mailBox) {
        super(actor.actorId(), mailBox);
        this.actor = actor;
        this.mailBox = mailBox;
    }

    @Override
    public void trigger(Message message) {
        if (shutdown) {
            throw new IllegalStateException("actor " + actor.actorId() + " is not live");
        }
        mailBox.send(message);
    }

    @Override
    public void execute(Message message) {
        try {
            if (Objects.equals(SYNC, actor.type())) {
                submit(message).get();
            }
        } catch (Throwable e) {
            throw new ExecutionError(e);
        }
    }

    private Future submit(Message message) {
        return ActorSystem.cpu().submit(() -> actor.onMessage(message));
    }
}