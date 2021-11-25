package kalva.mc.messaging.impl;

import kalva.mc.messaging.Record;
import kalva.mc.messaging.MessagingServer;
import kalva.mc.messaging.Service;

public class RemoteMessagingServer implements MessagingServer, Service {

    @Override
    public void send(Record record) {

    }

    @Override
    public Record consume(String clientId, String topic) {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
