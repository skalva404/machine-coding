package kalva.mc.messaging.rpc;

import kalva.mc.messaging.Status;
import kalva.mc.messaging.Worker;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;
import static kalva.mc.messaging.Status.SUCCESS;

@Slf4j
public class ConnectionHandler extends Worker {

    private Selector selector;
    private Map<SocketChannel, LinkedList<ByteBuffer>> dataMap = new HashMap<>();

    @lombok.SneakyThrows
    public ConnectionHandler(String name) {
        super(name);

    }

    @Override
    public void preExecute() throws Exception {
        init();
    }

    @Override
    public Status execute() {

        try {

            log.info("waiting for select...");
            int noOfKeys = selector.select();
            log.info("number of selected keys: " + noOfKeys);
            if (noOfKeys == 0) {
                log.warn("no activities found ...");
                return Status.BACKOFF;
            }
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {

                SelectionKey ky = selectedKeys.next();
                selectedKeys.remove();
                log.info("selected key {}", ky);

                if (!ky.isValid()) {
                    log.info("invalid key {}", ky);
                    continue;
                }
                if (ky.isAcceptable()) {
                    log.info("key is acceptable");
                    return accept(ky);
                } else if (ky.isReadable()) {
                    log.info("key is readable");
                    read(ky);
                } else if (ky.isWritable()) {
                    log.info("key is writable");
                    write(ky);
                }
            }
        } catch (Throwable e) {
            log.error("error processing selector request", e);
            return Status.BACKOFF;
        }
        return SUCCESS;
    }

    private void init() throws IOException {

        selector = SelectorProvider.provider().openSelector();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress("localhost", 5454));
        serverChannel.register(selector, OP_ACCEPT);
    }

    private Status accept(SelectionKey ky) throws IOException {
        var serverChannel = (ServerSocketChannel) ky.channel();
        SocketChannel clientChannel = serverChannel.accept();
        if (null == clientChannel) {
            log.info("no new connected from clients");
            return Status.BACKOFF;
        }
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, OP_READ);
        dataMap.putIfAbsent(clientChannel, new LinkedList<>());
        return SUCCESS;
    }

    private void read(SelectionKey ky) throws IOException {
        SocketChannel client = (SocketChannel) ky.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        client.read(buffer);
        String output = new String(buffer.array()).trim();
        log.info("message read from client: {}", output);
        if (null == dataMap.get(client)) {
            client.register(selector, SelectionKey.OP_READ);
            dataMap.putIfAbsent(client, new LinkedList<>());
        }
        dataMap.get(client).add(buffer);
    }

    private void write(SelectionKey ky) throws IOException {
        var client = (SocketChannel) ky.channel();
        String response = "hi - from non-blocking server";
        byte[] bs = response.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.wrap(bs);
        client.write(buffer);
        client.register(selector, SelectionKey.OP_WRITE);
        log.info("message sent to client: OK");
    }

    @Override
    public void shutdown() {
    }

    @Override
    public int sleepTime() {
        return 100;
    }
}
