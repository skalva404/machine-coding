package kalva.mc.messaging;

import kalva.mc.messaging.impl.TopicMetaData;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static kalva.mc.messaging.TestTopicFileImpl.*;

public class TestMetadata {

    @Test
    void simpleTest() throws IOException {

        TopicMetaData metadata = new TopicMetaData(TestTopicFileImpl.FOLDER);
        metadata.addClientInfo(CLIENT_1, new TopicMetaData.ClientInfo(CLIENT_1, 123L));
        metadata.addClientInfo(CLIENT_2, new TopicMetaData.ClientInfo(CLIENT_2, 321L));

        metadata.addFileInfo(TOPIC, new TopicMetaData.FileInfo(TOPIC, 0L, 99L));
        metadata.save();
    }
}
