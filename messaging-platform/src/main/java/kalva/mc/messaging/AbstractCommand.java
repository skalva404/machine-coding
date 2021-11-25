package kalva.mc.messaging;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.channels.SocketChannel;

@Getter
@Accessors(fluent = true)
public abstract class AbstractCommand implements Command {

    private final SocketChannel client;

    public AbstractCommand(SocketChannel client) {
        this.client = client;
    }
}
