package kalva.mc.actor.impl;

import kalva.mc.actor.LifeCycle;
import kalva.mc.actor.MailBox;
import kalva.mc.actor.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class TaskRunner extends Thread implements LifeCycle {

    private MailBox mailBox;
    volatile boolean shutdown = Boolean.FALSE;

    TaskRunner(String name, MailBox mailBox) {
        super(name);
        this.mailBox = mailBox;
    }

    @Override
    public void init() {
        this.start();
    }

    public void run() {
        try {
            while (!shutdown) {
                drainAndProcess(100);
            }
        } catch (Throwable ie) {
            log.warn("error while running ", ie);
        }
    }

    private void drainQueue() {
        try {
            log.info("draining the remaining messages");
            while (mailBox.ramaining() > 0) {
                drainAndProcess(1);
            }
        } catch (Throwable ignore) {
        }
    }

    private void drainAndProcess(int i) throws InterruptedException {
        Message message = mailBox.get(i, TimeUnit.MILLISECONDS);
        if (null == message) {
            return;
        }
        log.debug("submitting message {} by {}", new String(message.data()), getName());
        processMessage(message);
    }

    @Override
    public void close() {
        log.info("shutting down the task runner {}", getName());
        shutdown = Boolean.TRUE;
        drainQueue();
    }

    public abstract void processMessage(Message message);
}
