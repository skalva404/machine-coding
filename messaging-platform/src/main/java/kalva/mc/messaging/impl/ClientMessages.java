package kalva.mc.messaging.impl;

import kalva.mc.messaging.Message;
import kalva.mc.messaging.impl.TopicMetaData.TopicDetails;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

@Getter
@Accessors(fluent = true)
public class ClientMessages {

    private String clientId;
    private Long currentOffset;
    private TopicMetaData metadata;
    private Queue<Message> messagesFromTopic;

    public ClientMessages(String clientId, Long currentOffset,
                          TopicMetaData metadata) throws IOException {
        this.clientId = clientId;
        this.metadata = metadata;
        this.currentOffset = currentOffset;
        reLoadMessage(metadata.read());
    }

    public void reLoadMessage(TopicDetails topicDetails) throws IOException {
        TopicMetaData.FileInfo file = null;
        Collection<TopicMetaData.FileInfo> files = topicDetails.files().values();
        for (TopicMetaData.FileInfo fileInfo : files) {
            String fileName = logFileName(fileInfo);
            String[] split;
            split = fileName.split("-");
            Long start = Long.parseLong(split[0]);
            Long end = Long.parseLong(split[1]);
            if ((currentOffset + 1) >= start && (currentOffset + 1) <= end) {
                file = fileInfo;
                break;
            }
        }
        //TODO consider reading from a active file.
        if (null != file) {
            String fileName = file.fileName();
            List<String> messages = Files.readAllLines(Path.of(fileName));
            messagesFromTopic = new ArrayDeque<>();
            for (String s : messages) {
                String[] split = s.split(":");
                messagesFromTopic.add(new Message(Long.parseLong(split[0]), split[1].getBytes()));
            }
        }
    }

    private String logFileName(TopicMetaData.FileInfo fileInfo) {
        String completeFileName = fileInfo.fileName();
        String[] split = completeFileName.split("/");
        return split[split.length - 1].replace(".log", "");
    }

    public Message next() throws IOException {
        if (null == messagesFromTopic || 0 == messagesFromTopic.size()) {
            reLoadMessage(metadata.read());
        }
        if (null == messagesFromTopic || 0 == messagesFromTopic.size()) {
            return null;
        }
        Message poll = messagesFromTopic.poll();
        if (null == poll) {
            return null;
        }
        currentOffset = poll.offsetId();
        return poll;
    }
}
