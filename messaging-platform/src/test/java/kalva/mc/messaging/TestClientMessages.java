package kalva.mc.messaging;

import kalva.mc.messaging.impl.ClientMessages;
import kalva.mc.messaging.impl.TopicMetaData;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestClientMessages {

    @Test
    void simpleTest() throws IOException {


        TopicMetaData metadata = new TopicMetaData(TestTopicFileImpl.FOLDER);
        ClientMessages clientMessages = new ClientMessages(TestTopicFileImpl.CLIENT_1, 0L, metadata);
        Message next = clientMessages.next();
        while (null != next) {
            System.out.println(next);
            next = clientMessages.next();
        }
    }
}
