package kalva.mc.messaging.commands;

import kalva.mc.messaging.AbstractCommand;
import kalva.mc.messaging.Result;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import static java.nio.ByteBuffer.wrap;
import static kalva.mc.messaging.Command.Type.PRODUCE;

public class Connect extends AbstractCommand {

    public Connect(SocketChannel client) {
        super(client);
    }

    @Override
    public Result execute() throws IOException {
        client().write(wrap(PRODUCE.toString().getBytes()));
        return Result.EMPTY;
    }
}