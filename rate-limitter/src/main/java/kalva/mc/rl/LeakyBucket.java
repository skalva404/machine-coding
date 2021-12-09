package kalva.mc.rl;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class LeakyBucket implements RateLimiter {

    private int maxRequestPerSec;
    private long nextAllowedTime;
    private final long REQUEST_INTERVAL_MILLIS;

    public LeakyBucket(int maxRequestPerSec) {
        this.maxRequestPerSec = maxRequestPerSec;
        REQUEST_INTERVAL_MILLIS = 1000 / maxRequestPerSec;
        nextAllowedTime = System.currentTimeMillis();
    }

    @Override
    public boolean allow() {
        long curTime = System.currentTimeMillis();
        synchronized (this) {
            if (curTime >= nextAllowedTime) {
                nextAllowedTime = curTime + REQUEST_INTERVAL_MILLIS;
                return true;
            }
            return false;
        }
    }
}
