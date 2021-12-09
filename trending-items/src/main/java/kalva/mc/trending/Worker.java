package kalva.mc.trending;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Setter
@Accessors(fluent = true)
public abstract class Worker<I, O> extends Thread implements Service {

    protected AtomicBoolean stop = new AtomicBoolean(false);
    protected AtomicBoolean stopped = new AtomicBoolean(false);
    protected AtomicBoolean emitEventFlag = new AtomicBoolean(false);

    protected BlockingDeque<I> inputQueue;
    protected BlockingDeque<O> outputQueue;

    public Worker(String name) {
        super(name);
    }

    public void close() {
        log.info("stopping the {} worker", getName());
        stop.set(true);
    }

    @Override
    public void run() {
        while (!stop.get() && !stopped.get()) {
            if (stop.get()) {
                log.warn("issued stop signal, waiting for worker to stop the work...");
            }
            try {
                Status execute = execute();
                if (execute == Status.BACKOFF) {
//                    log.warn("Worker is backing off " + getName());
                    TimeUnit.MILLISECONDS.sleep(500);
                }
            } catch (Exception e) {
                log.error("ignoring the errors for now ", e);
            }
        }
    }

    public void emitEvents() {
        execute();
        emitEventFlag.set(true);
    }

    public abstract Status execute();
}