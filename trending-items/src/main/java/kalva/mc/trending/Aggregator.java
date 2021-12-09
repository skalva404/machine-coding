package kalva.mc.trending;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;

@Slf4j
@Setter
@Accessors(fluent = true)
public class Aggregator extends Worker<Tuple, Tuple> {

    private SlidingWindow<String> counter;

    public Aggregator(String name, Integer windowSize, BlockingDeque<Tuple> inputQueue,
                      BlockingDeque<Tuple> outputQueue) {
        super(name);
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        counter = new SlidingWindow<>(windowSize);
    }

    @Override
    public synchronized Status execute() {
        if (emitEventFlag.get()) {
            Map<String, Long> window = counter.getCountsThenAdvanceWindow();
            window.forEach((s, aLong) -> {
                Tuple tuple = new Tuple(s, aLong);
                log.info("emitting {} aggregation details with {}", getName(), tuple);
                outputQueue.add(tuple);
            });
            emitEventFlag.set(false);
            return Status.SUCCESS;
        }
        Tuple poll = inputQueue.poll();
        if (null != poll) {
//            log.info("{} received event {}", getName(), poll);
            counter.incrementCount(poll.key());
            return Status.SUCCESS;
        } else {
            return Status.BACKOFF;
        }
    }
}
