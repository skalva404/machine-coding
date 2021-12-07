package kalva.mc.cron;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DummyTask implements Task {

    @Override
    public synchronized void execute(TaskContext context) {
        Shared.executed.get(Shared.count++).set(true);
        log.info("executing the task {}", context.toString());
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}