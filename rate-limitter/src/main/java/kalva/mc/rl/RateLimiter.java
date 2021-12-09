package kalva.mc.rl;

public interface RateLimiter {

    int maxRequestPerSec();

    boolean allow();
}
