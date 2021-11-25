package kalva.mc.messaging;

import kalva.mc.messaging.rpc.ConnectionHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Slf4j
public class TestConnectionHandler {

    @Test
    void simpleTest() throws InterruptedException {

        ConnectionHandler handler = new ConnectionHandler("ConnectionHandler");
        handler.start();
        Thread.sleep(5000);

        String[] messages = new String[]{"Time goes fast.", "What now?", "Bye."};
        for (String message : messages) {
            new Thread(message) {
                @SneakyThrows
                @Override
                public void run() {
                    runClient(message);
                }
            }.start();
        }

        Thread.sleep(15000);
        handler.shutdown();
    }

    private void runClient(String message) throws IOException, InterruptedException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 5454);
        SocketChannel client = SocketChannel.open(hostAddress);
        byte[] bytes = message.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        client.write(buffer);
        log.info("Client sent message to server {}", message);
        buffer.clear();

//            ByteBuffer inBuf = ByteBuffer.allocate(1024);
//            while (client.read(inBuf) > 0) {
//                System.out.printf("[%s]:\t%s\n", currentThread().getName(),
//                        new String(inBuf.array(), UTF_8));
//            }
        client.close();
    }
}
