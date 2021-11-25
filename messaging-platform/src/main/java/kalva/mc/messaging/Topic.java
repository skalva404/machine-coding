package kalva.mc.messaging;

import java.io.IOException;

public interface Topic {

    void write(Record record) throws IOException;

    Record read(String clientId) throws IOException;

    void commit(String clientId, Long offsetId) throws IOException;

    void subscribe(String clientId) throws IOException;
}
