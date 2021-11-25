package kalva.mc.messaging.impl;

import kalva.mc.messaging.Record;
import kalva.mc.messaging.Topic;
import kalva.mc.messaging.impl.TopicMetaData.ClientInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicFileImpl implements Topic {

    private String name;
    private String folder;
    private Integer currentFileCount = 0;

    private TopicMetaData metadata;
    private Long currentOffset;
    private TopicMetaData.TopicDetails topicDetails;
    private List<String> messages = new ArrayList<>();
    private Map<String, ClientMessages> subscribedClients;

    private Long prevOffset = 0L;

    public TopicFileImpl(String folder, String name) throws IOException {
        this.name = name;
        this.folder = folder;
        metadata = new TopicMetaData(folder + "/" + name);
        topicDetails = metadata.read();
        if (null == topicDetails) {
            topicDetails = new TopicMetaData.TopicDetails(new HashMap<>(), new HashMap<>(), 1L);
        }
        currentOffset = topicDetails.latestOffset();
        if (null == currentOffset) {
            currentOffset = 1L;
        }
        subscribedClients = new HashMap<>();
    }

    @Override
    public synchronized void write(Record record) throws IOException {
        if (currentFileCount > 100) {
            flush();
        }
        messages.add(new String(record.body()));
        currentFileCount++;
    }

    public synchronized void flush() throws IOException {
        if (null == messages || 0 == messages.size()) {
            return;
        }
        Long localCounter = currentOffset;
        StringBuilder sb = new StringBuilder();
        for (String event : messages) {
            sb.append(localCounter++).append(":").append(event).append("\n");
        }
        Path fileName = getFileName();
        Files.writeString(fileName, sb.toString());
        metadata.addFileInfo(getFileName().toString(),
                new TopicMetaData.FileInfo(getFileName().toString(),
                        currentOffset, currentFileCount + currentOffset - 1));
        currentOffset = currentFileCount + currentOffset;
        metadata.setLatestOffset(currentOffset);
        saveMetaData();
        currentFileCount = 0;
        messages = new ArrayList<>();
    }

    private void saveMetaData() throws IOException {
        metadata.save();
        topicDetails = metadata.read();
    }

    private Path getFileName() {
        return Path.of(folder, name + "/" + currentOffset + "-" + (currentFileCount + currentOffset - 1) + ".log");
    }

    @Override
    public Record read(String clientId) throws IOException {
        ClientMessages clientMessages = subscribedClients.get(clientId);
        if (null == clientMessages) {
            throw new RuntimeException("Client id " + clientId + " not subscribed");
        }
        Record next = clientMessages.next();
        while (null != next && next.offsetId() <= prevOffset) {
            next = clientMessages.next();
        }
        return next;
    }

    @Override
    public void subscribe(String clientId) throws IOException {
        topicDetails = metadata.read();
        ClientInfo clientInfo;
        if (null == topicDetails.clients() ||
                null == topicDetails.clients().get(clientId)) {
            clientInfo = new ClientInfo(clientId, 0L);
            topicDetails.addClientInfo(clientId, clientInfo);
            metadata.addClientInfo(clientId, clientInfo);
            metadata.save();
        }
        clientInfo = topicDetails.clients().get(clientId);
        prevOffset = clientInfo.offset();
        subscribedClients.put(clientId, new ClientMessages(clientId, clientInfo.offset(), metadata));
    }

    @Override
    public void commit(String clientId, Long offsetId) throws IOException {
        ClientInfo clientInfo = metadata.topicDetails().clients().get(clientId);
        if (null == clientInfo) {
            throw new RuntimeException("Client id " + clientId + " not subscribed");
        }
        clientInfo = new ClientInfo(clientId, offsetId);
        topicDetails.addClientInfo(clientId, clientInfo);
        metadata.addClientInfo(clientId, clientInfo);
        metadata.save();
    }
}
