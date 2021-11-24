package kalva.mc.messaging;

import java.io.IOException;

public interface Topic {

    void write(Message message) throws IOException;

    Message read(String clientId) throws IOException;

    void commit(String clientId, Long offsetId) throws IOException;

    void subscribe(String clientId) throws IOException;
}
