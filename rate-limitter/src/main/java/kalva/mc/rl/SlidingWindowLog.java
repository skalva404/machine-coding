package kalva.mc.rl;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Accessors(fluent = true)
public class SlidingWindowLog implements RateLimiter {

    private final int maxRequestPerSec;
    private final ConcurrentMap<Long, AtomicInteger> windows = new ConcurrentHashMap<>();

    public SlidingWindowLog(int maxRequestPerSec) {
        this.maxRequestPerSec = maxRequestPerSec;
    }

    @Override
    public boolean allow() {
        long curTime = System.currentTimeMillis();
        long curWindowKey = curTime / 1000 * 1000;
        windows.putIfAbsent(curWindowKey, new AtomicInteger(0));
        long preWindowKey = curWindowKey - 1000;
        AtomicInteger preCount = windows.get(preWindowKey);
        if (preCount == null) {
            return windows.get(curWindowKey).incrementAndGet() <= maxRequestPerSec;
        }

        double preWeight = 1 - (curTime - curWindowKey) / 1000.0;
        long count = (long) (preCount.get() * preWeight
                + windows.get(curWindowKey).incrementAndGet());
        return count <= maxRequestPerSec;
    }
}
