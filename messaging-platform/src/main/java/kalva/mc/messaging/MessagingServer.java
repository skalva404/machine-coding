package kalva.mc.messaging;

public interface MessagingServer {

    void send(Message message);

    Message consume(String clientId, String topic);

}
