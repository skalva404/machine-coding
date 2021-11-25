package kalva.mc.messaging;

import kalva.mc.messaging.commands.Connect;
import kalva.mc.messaging.rpc.ConnectionHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class TestCommands {

    @Test
    void simpleTest() throws IOException, InterruptedException {

        ConnectionHandler handler = new ConnectionHandler("ConnectionHandler");
        handler.start();
        Thread.sleep(3000);

        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 5454);
        SocketChannel client = SocketChannel.open(hostAddress);
        Command command = new Connect(client);
        command.execute();

        Thread.sleep(3000);
    }
}
