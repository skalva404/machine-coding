package kalva.mc.messaging;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface Command {

    Result execute() throws IOException;

    public enum Type {
        PRODUCE, SUBSCRIBE, SEND, CONSUME
    }
}
