package server.core;

import server.util.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public final class Listener {
    private static Listener instance;
    private static final Logger logger = Logger.getInstance();
    private static final ServerConfiguration config = ServerConfiguration.getInstance();
    private static final Dispatcher dispatcher = Dispatcher.getInstance();

    private static Selector selector;
    private static ServerSocketChannel channel;

    private Listener() {
        try {
            selector = Selector.open();
            channel = ServerSocketChannel.open();
        } catch (IOException ignored) {
            logger.err("Failed to start the server.");
        }
    }

    public static Listener getInstance() {
        if (instance == null)
            instance = new Listener();
        return instance;
    }

    public void setup() throws IOException {
        channel.socket().bind(new InetSocketAddress(config.getPort()));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void listen() {
        try {
            selector.select();

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (key.isAcceptable())
                    accept(key);
                else if (key.isReadable())
                    read(key);
                else
                    throw new IOException();
            }
        } catch (IOException e) {
            logger.err("Internal server error: " + e.getMessage());
        }
    }

    @SuppressWarnings("resource")
    private void accept(SelectionKey key) {
        try {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel clientChannel = serverChannel.accept();
            SocketAddress address = clientChannel.getRemoteAddress();
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);

            logger.out(address + " connected.");
        } catch (Exception e) {
            logger.err("Failed to accept connection: " + e.getMessage());
        }
    }

    private void read(SelectionKey key) {
        dispatcher.dispatch(key);
    }
}
