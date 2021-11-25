package kalva.mc.messaging;

public interface MessagingServer {

    void send(Record record);

    Record consume(String clientId, String topic);

}
