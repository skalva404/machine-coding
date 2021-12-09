import com.google.common.collect.Lists;
import kalva.mc.trending.Aggregator;
import kalva.mc.trending.Ranker;
import kalva.mc.trending.Topology;
import kalva.mc.trending.Tuple;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class DemoTest {

    @Test
    public void basicTest() throws InterruptedException {

        BlockingDeque<Tuple> inputQueue = new LinkedBlockingDeque<>();
        BlockingDeque<Tuple> localAggQueue = new LinkedBlockingDeque<>();
        BlockingDeque<Tuple> rankQueue = new LinkedBlockingDeque<>();

        Aggregator localAgg1 = new Aggregator("localAgg1", 5, inputQueue, localAggQueue);
        Aggregator localAgg2 = new Aggregator("localAgg2", 5, inputQueue, localAggQueue);
        Ranker ranker = new Ranker("ranker", localAggQueue, rankQueue);

        Topology topology = Topology.create()
                .tickleSecs(6)
                .ranker(ranker)
                .addAggregator(localAgg1)
                .addAggregator(localAgg2);
        topology.start();

        Map<String, Integer> itemMap = new HashMap<>();
        List<String> topics = Lists.newArrayList("sunil", "kalva");//, "c", "d", "e"
        Thread writerThread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                int counter = 0;
                Random random = new Random();
                while (counter++ < 10) {
                    String s = topics.get(random.nextInt(topics.size()));
                    inputQueue.add(new Tuple(s, 0L));
                    Integer integer = itemMap.getOrDefault(s, 0);
                    itemMap.put(s, ++integer);
                    Thread.sleep(500);
                }
            }
        });

        Thread aggregatorThread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    while (rankQueue.size() > 0) {
                        Tuple poll = rankQueue.poll();
                        if (null == poll) {
                            continue;
                        }
//                        System.out.println("Ranking :: " + poll);
                    }
                    Thread.sleep(100);
                }
            }
        });

        writerThread.start();
        aggregatorThread.start();
        TimeUnit.SECONDS.sleep(10);
        System.out.println(itemMap);
    }
}


////        aggregator1.start();
////        aggregator2.start();
////        ranker.start();
//
//        writerThread.start();
//                aggregatorThread.start();
//
////        TimeUnit.SECONDS.sleep(2);
////        reader.emitEvents();
////        TimeUnit.SECONDS.sleep(1);
////        ranker1.emitEvents();
////        ranker2.emitEvents();
//
////        TimeUnit.SECONDS.sleep(10);
////        aggregator1.emitEvents();
////        aggregator2.emitEvents();
//                TimeUnit.SECONDS.sleep(10);
////        ranker.emitEvents();
//
//                TimeUnit.SECONDS.sleep(2);
//                System.out.println(itemMap);