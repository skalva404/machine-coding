package kalva.mc.messaging;

import kalva.mc.messaging.impl.TopicFileImpl;
import kalva.mc.messaging.impl.TopicMetaData;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestTopicFileImpl {

    public static String ROOT = "./logs/";

    public static String TOPIC = "demo";
    public static String FOLDER = ROOT + TOPIC;

    public static String CLIENT_1 = "client-1";
    public static String CLIENT_2 = "client-2";

    @Test
    void simpleTest() throws IOException {

        File file = new File(FOLDER);
        if (file.exists()) {
            Files.walk(Path.of(FOLDER), 2)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        file.mkdirs();

        TopicFileImpl topic = new TopicFileImpl(ROOT, TOPIC);

        for (int i = 1; i <= 100; i++) {
            topic.write(new Message(("testing " + i).getBytes()));
        }
        topic.flush();

        topic.subscribe(CLIENT_1);
        topic.subscribe(CLIENT_2);

        consume(topic, 10, 10);
        consume(topic, 90, 100);
    }

    private void consume(TopicFileImpl topic, int n, int offset) throws IOException {
        Message read1 = null;
        Message read2 = null;
        for (int i = 1; i <= n; i++) {
            read1 = topic.read(CLIENT_1);
            read2 = topic.read(CLIENT_2);
            assertNotNull(read1);
            assertNotNull(read2);
        }
        validate(topic, read1, read2, offset);
    }

    private void validate(TopicFileImpl topic, Message read1, Message read2, int offset) throws IOException {
        assertEquals(offset, read1.offsetId());
        assertEquals(offset, read2.offsetId());
        topic.commit(CLIENT_1, read1.offsetId());
        topic.commit(CLIENT_2, read2.offsetId());
        TopicMetaData metadata = new TopicMetaData(FOLDER);
        TopicMetaData.TopicDetails topicDetails = metadata.read();
        assertEquals(offset, topicDetails.clients().get(CLIENT_1).offset());
        assertEquals(offset, topicDetails.clients().get(CLIENT_2).offset());
    }
}
