package kalva.mc.cron;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class TaskPoolExecutor extends ThreadPoolExecutor {

    public TaskPoolExecutor(int maximumPoolSize, int bufferSize) {
        super(maximumPoolSize, maximumPoolSize, 60, SECONDS,
                new ArrayBlockingQueue<>(bufferSize),
                (r, executor) -> {
                    throw new RuntimeException("no threads are available for executing tasks");
                });
    }
}
