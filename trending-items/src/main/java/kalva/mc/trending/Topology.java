package kalva.mc.trending;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Long.valueOf;

@Slf4j
public class Topology implements Service {

    private Ranker ranker;
    private Integer tickleSecs = 1;
    private List<Aggregator> aggregators;
    private ScheduledExecutorService service;

    private Topology() {
        aggregators = new ArrayList<>();
        service = Executors.newScheduledThreadPool(2);
    }

    public static Topology create() {
        return new Topology();
    }

    public Topology tickleSecs(Integer tickleSecs) {
        this.tickleSecs = tickleSecs;
        return this;
    }

    public Topology ranker(Ranker ranker) {
        this.ranker = ranker;
        return this;
    }

    public Topology addAggregator(Aggregator aggregator) {
        aggregators.add(aggregator);
        return this;
    }

    public void start() {
        ranker.start();
        for (Aggregator aggregator : aggregators) {
            aggregator.start();
        }
        service.scheduleWithFixedDelay(() -> aggregators.parallelStream().forEach(aggregator -> {
            log.info("triggering tickle event for {}", aggregator.getName());
            aggregator.emitEvents();
        }), valueOf(tickleSecs), valueOf(tickleSecs), TimeUnit.SECONDS);

        service.scheduleWithFixedDelay(() -> {
            log.info("triggering tickle event for {}", ranker.getName());
            ranker.emitEvents();
        }, 2 + tickleSecs, 2 + tickleSecs, TimeUnit.SECONDS);
    }

    @Override
    public void close() {
        service.shutdown();
        ranker.close();
        for (Aggregator aggregator : aggregators) {
            aggregator.close();
        }
    }
}
