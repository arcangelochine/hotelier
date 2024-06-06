package server.core;

import server.util.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public final class AcceptHandler implements EventHandler {
    private static final Logger logger = Logger.getInstance();

    @Override
    public void handle(SelectionKey key) {
        try {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel clientChannel = serverChannel.accept();
            Selector selector = key.selector();

            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);

            logger.out(clientChannel.getRemoteAddress() + " connected");
        } catch (IOException e) {
            logger.err("Failed to accept connection: " + e.getMessage());
        }
    }
}
