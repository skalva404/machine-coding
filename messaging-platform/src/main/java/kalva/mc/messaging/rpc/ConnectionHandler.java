package kalva.mc.messaging.rpc;

import kalva.mc.messaging.Status;
import kalva.mc.messaging.Worker;

import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class ConnectionHandler extends Worker {

    private Selector selector;
    private ServerSocketChannel serverSocket;

    @lombok.SneakyThrows
    public ConnectionHandler(String name) {
        super(name);
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", 5454));
    }

    @Override
    public void shutdown() {

    }

    @Override
    public Status execute() {
        return null;
    }
}
