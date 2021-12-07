package kalva.mc.cron.q;

import kalva.mc.cron.FairnessStrategy;

import java.util.ArrayDeque;

import static kalva.mc.cron.FairnessStrategy.Earliest;

public class TaskQueue<T> {

    private FairnessStrategy strategy;
    private final ArrayDeque<T> queue;

    public TaskQueue() {
        this.queue = new ArrayDeque<>();
    }

    public TaskQueue(int capacity) {
        this(capacity, Earliest);
    }

    public TaskQueue(int capacity, FairnessStrategy strategy) {
        this.strategy = strategy;
        this.queue = new ArrayDeque<>(capacity);
    }

    public boolean send(T task) {
        if (Earliest.equals(strategy)) {
            return queue.offerLast(task);
        } else {
            return queue.offerFirst(task);
        }
    }

    public T consume() {
        if (Earliest.equals(strategy)) {
            return queue.pollFirst();
        } else {
            return queue.pollLast();
        }
    }

    public Integer getCurrentCount() {
        return queue.size();
    }

    public void clear() {
        queue.clear();
    }
}
