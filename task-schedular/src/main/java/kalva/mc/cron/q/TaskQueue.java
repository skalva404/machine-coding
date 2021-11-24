package kalva.mc.cron.q;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TaskQueue<T> {

    private final BlockingQueue<T> queue;

    public TaskQueue() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public TaskQueue(int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity);
    }

    public boolean send(T task) {
        return queue.offer(task);
    }

    public boolean send(T task, long timeout, TimeUnit unit) throws InterruptedException {
        return queue.offer(task, timeout, unit);
    }

    public T consume() {
        return queue.poll();
    }

    public T consume(long timeout, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeout, unit);
    }

    public Integer getCurrentCount() {
        return queue.size();
    }
}
