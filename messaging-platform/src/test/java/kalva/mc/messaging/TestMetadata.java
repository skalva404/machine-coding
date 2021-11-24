package kalva.mc.messaging;

import kalva.mc.messaging.impl.TopicFileImpl;
import kalva.mc.messaging.impl.TopicMetaData;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestMetadata {

    @Test
    void simpleTest_1() throws IOException {
        TopicFileImpl topic = new TopicFileImpl("/Users/sunilkumar.kalva/workspace/learnings/interviews/messaging-platform" +
                "/src/test/java/kalva/mc/messaging/", "demo");
        for (int i = 1; i <= 100; i++) {
            topic.write(new Message(("testing " + i).getBytes()));
        }
        topic.flush();
    }

    @Test
    void simpleTest() throws IOException {

        TopicMetaData metadata = new TopicMetaData("/Users/sunilkumar.kalva/workspace/learnings/interviews/messaging-platform" +
                "/src/test/java/kalva/mc/messaging/demo");
        metadata.addClientInfo("abc", new TopicMetaData.ClientInfo("abc", 123L));
        metadata.addClientInfo("pqr", new TopicMetaData.ClientInfo("pqr", 321L));

        metadata.addFileInfo("one", new TopicMetaData.FileInfo("one", 0L, 99L));
        metadata.save();
    }
}
