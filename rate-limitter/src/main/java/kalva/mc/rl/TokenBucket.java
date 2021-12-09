package kalva.mc.rl;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

@Getter
@Accessors(fluent = true)
public class TokenBucket implements RateLimiter {

    private int tokens;
    private final int maxRequestPerSec;

    public TokenBucket(int maxRequestPerSec) {
        this.maxRequestPerSec = maxRequestPerSec;
        this.tokens = maxRequestPerSec;
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refillTokens(maxRequestPerSec);
            }
        }).start();
    }

    @Override
    public boolean allow() {
        synchronized (this) {
            if (tokens == 0) {
                return false;
            }
            tokens--;
            return true;
        }
    }

    private void refillTokens(int cnt) {
        synchronized (this) {
            tokens = maxRequestPerSec;
            tokens = Math.min(tokens + cnt, maxRequestPerSec);
            notifyAll();
        }
    }
}
