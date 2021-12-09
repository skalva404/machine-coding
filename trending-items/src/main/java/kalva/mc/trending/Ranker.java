package kalva.mc.trending;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingDeque;

import static java.lang.Long.valueOf;

@Slf4j
public class Ranker extends Worker<Tuple, Tuple> {

    private Rankings rankings;

    public Ranker(String name, BlockingDeque<Tuple> inputQueue, BlockingDeque<Tuple> outputQueue) {
        super(name);
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.rankings = new Rankings(Integer.MAX_VALUE);
    }

    @Override
    public synchronized Status execute() {
        if (emitEventFlag.get()) {
            rankings.pruneZeroCounts();
            rankings.getRankings().forEach(rank -> {
                Tuple tuple = new Tuple(rank.keyword(), valueOf(rankings.rankOf(rank)));
                log.info("emitting {} details for {} => {}", getName(), rank, tuple);
                outputQueue.add(tuple);
            });
            rankings = new Rankings(Integer.MAX_VALUE);
            emitEventFlag.set(false);
            return Status.SUCCESS;
        }
        Tuple poll = inputQueue.poll();
        if (null != poll) {
//            log.info("{} received event {}", getName(), poll);
            rankings.updateWith(new RankableKeyword(poll.key(), poll.count()));
            return Status.SUCCESS;
        } else {
            return Status.BACKOFF;
        }
    }
}
