package kalva.mc.actor.impl;

import kalva.mc.actor.MailBox;
import kalva.mc.actor.error.MailboxFullError;
import kalva.mc.actor.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SimpleMailBox implements MailBox {

    private int boxSize;
    private ArrayBlockingQueue<Message> queue;

    public SimpleMailBox(int boxSize) {
        this.boxSize = boxSize;
        queue = new ArrayBlockingQueue<>(boxSize);
    }

    @Override
    public void send(Message message) {
        try {
            queue.add(message);
        } catch (IllegalStateException ise) {
            throw new MailboxFullError("Mailbox is full " + boxSize);
        }
    }

    @Override
    public Message get() throws InterruptedException {
        return queue.take();
    }

    @Override
    public Message get(long timeout, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeout, unit);
    }

    @Override
    public Integer ramaining() {
        return queue.size();
    }
}
