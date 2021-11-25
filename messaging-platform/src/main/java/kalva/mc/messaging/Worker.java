package kalva.mc.messaging;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class Worker extends Thread implements Service {

    protected AtomicBoolean stop = new AtomicBoolean(false);

    public Worker(String name) {
        super(name);
    }

    public void shutdown() {
        log.info("stopping the {} worker", getName());
        stop.set(true);
    }

    @Override
    @SneakyThrows
    public void run() {
        preExecute();
        while (!stop.get()) {
            try {
                Status execute = execute();
                if (execute == Status.BACKOFF) {
                    log.warn("{} worker is backing off ", getName());
                    TimeUnit.MILLISECONDS.sleep(5L * sleepTime());
                }
                TimeUnit.MILLISECONDS.sleep(sleepTime());
            } catch (Exception e) {
                log.error("ignoring the errors for now ", e);
            }
        }
    }

    public abstract int sleepTime();

    public void preExecute() throws Exception {

    }

    public abstract Status execute();
}