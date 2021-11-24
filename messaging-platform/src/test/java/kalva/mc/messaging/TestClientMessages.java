package kalva.mc.messaging;

import kalva.mc.messaging.impl.TopicFileImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestClientMessages {

    @Test
    void simpleTest() throws IOException {

        TopicFileImpl topic = new TopicFileImpl("/Users/sunilkumar.kalva/workspace/learnings/interviews/messaging-platform" +
                "/src/test/java/kalva/mc/messaging/", "demo");
//        for (int i = 1; i <= 100; i++) {
//            topic.write(new Message(("testing " + i).getBytes()));
//        }
//        topic.flush();

        topic.subscribe("demo-client");
        Message read = null;
        for (int i = 1; i <= 50; i++) {
            read = topic.read("demo-client");
            System.out.println(read);
        }
        if (null != read) {
            topic.commit("demo-client", read.offsetId());
        }

//        System.out.println("=============================");
//        topic.subscribe("demo-client-1");
//        read = topic.read("demo-client-1");
//        for (int i = 1; i <= 10; i++) {
//            System.out.println(read);
//            read = topic.read("demo-client-1");
//        }
//        if (null != read) {
//            topic.commit("demo-client-1", read.offsetId());
//        }

        //        TopicMetaData metadata = new TopicMetaData("/Users/sunilkumar.kalva/workspace/learnings/interviews/messaging-platform" +
//                "/src/test/java/kalva/mc/messaging/demo");
//        ClientMessages clientMessages = new ClientMessages("demo", 0L, metadata);
//        Message next = clientMessages.next();
//        while (null != next) {
//            System.out.println(next);
//            next = clientMessages.next();
//        }
    }
}
