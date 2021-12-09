package kalva.mc.trending;

import java.util.concurrent.BlockingDeque;

public class FinalRanker extends Worker<Rankable, Rankable> {

    private Rankings rankings;

    public FinalRanker(String name, BlockingDeque<Rankable> inputQueue, BlockingDeque<Rankable> outputQueue) {
        super(name);
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.rankings = new Rankings(Integer.MAX_VALUE);
    }

    @Override
    public Status execute() {
        return null;
    }
}
