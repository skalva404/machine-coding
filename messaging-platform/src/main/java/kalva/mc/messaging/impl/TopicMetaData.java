package kalva.mc.messaging.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Getter
@Accessors(fluent = true)
public class TopicMetaData {

    private Gson gson;
    private static GsonBuilder gsonBuilder;

    private Path fileName;
    private TopicDetails topicDetails;

    public TopicMetaData(String name) throws IOException {

        gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
        fileName = Path.of(name);
        if (!fileName.toFile().exists()) {
            fileName.toFile().mkdirs();
        }
        fileName = Path.of(name + "/" + "metadata.info");
        if (!fileName.toFile().exists()) {
            fileName.toFile().createNewFile();
        }
        topicDetails = read();
        if (null == topicDetails) {
            topicDetails = new TopicDetails(new HashMap<>(), new HashMap<>(), 0L);
        }
    }

    public synchronized void save() throws IOException {
        Files.writeString(fileName, gson.toJson(topicDetails));
    }

    public synchronized TopicDetails read() throws IOException {
        return gson.fromJson(Files.readString(fileName), TopicDetails.class);
    }


    public void setLatestOffset(Long latestOffset) {
        topicDetails.setLatestOffset(latestOffset);
    }

    public void addFileInfo(String fileName, FileInfo fileInfo) {
        topicDetails.addFileInfo(fileName, fileInfo);
    }

    public void addClientInfo(String clientId, ClientInfo clientInfo) {
        topicDetails.addClientInfo(clientId, clientInfo);
    }

    @Getter
    @Accessors(fluent = true)
    public static class FileInfo {
        private String fileName;
        private Long start;
        private Long end;

        public FileInfo(String fileName, Long start, Long end) {
            this.fileName = fileName;
            this.start = start;
            this.end = end;
        }
    }

    @Getter
    @Accessors(fluent = true)
    public static class ClientInfo {
        private String clientId;
        private Long offset;

        public ClientInfo(String clientId, Long offset) {
            this.clientId = clientId;
            this.offset = offset;
        }
    }

    @Getter
    @Accessors(fluent = true)
    public static class TopicDetails {

        private Long latestOffset = 1L;
        private FileInfo latestFileInfo;
        private Map<String, FileInfo> files;
        private Map<String, ClientInfo> clients;

        public TopicDetails(Map<String, FileInfo> files, Map<String, ClientInfo> clients, Long latestOffset) {
            this.files = files;
            this.clients = clients;
            this.latestOffset = latestOffset;
        }

        public void setLatestOffset(Long latestOffset) {
            this.latestOffset = latestOffset;
        }

        public void addFileInfo(String fileName, FileInfo fileInfo) {
            if (null == files) {
                files = new HashMap<>();
            }
            files.put(fileName, fileInfo);
            latestFileInfo = fileInfo;
        }

        public void addClientInfo(String clientId, ClientInfo clientInfo) {
            if (null == clients) {
                clients = new HashMap<>();
            }
            clients.put(clientId, clientInfo);
        }
    }
}
