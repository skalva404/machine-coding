package kalva.mc.rl;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Accessors(fluent = true)
public class FixedWindowCounter implements RateLimiter {

    private final int maxRequestPerSec;
    private final ConcurrentMap<Long, AtomicInteger> windows = new ConcurrentHashMap<>();

    public FixedWindowCounter(int maxRequestPerSec) {
        this.maxRequestPerSec = maxRequestPerSec;
    }

    @Override
    public boolean allow() {
        long windowKey = System.currentTimeMillis() / 1000 * 1000;
        windows.putIfAbsent(windowKey, new AtomicInteger(0));
        return windows.get(windowKey).incrementAndGet() <= maxRequestPerSec;
    }
}
