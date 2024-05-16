package client.core;

import client.util.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Listener {
    private static Listener instance;

    private static final Logger logger = Logger.getInstance();
    private static final ClientConfiguration config = ClientConfiguration.getInstance();
    private static final ResponseHandler responseHandler = new ResponseHandler();
    private static final RequestHandler requestHandler = RequestHandler.getInstance();

    private static Selector selector;
    private static SocketChannel channel;

    private Listener() {
        try {
            selector = Selector.open();
            channel = SocketChannel.open();
            channel.connect(new InetSocketAddress(config.getHost(), config.getPort()));
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
            requestHandler.setChannel(channel);
        } catch (Exception e) {
            logger.err("Failed to connect to server: " + e.getMessage());
        }
    }

    public void listen() {
        try {
            while (channel.isConnected()) {
                selector.select();

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isReadable())
                        read(key);
                    else throw new IOException();
                }
            }
        } catch (Exception e) {
            logger.err("Client error: " + e.getMessage());
        }
    }

    public static Listener getInstance() {
        if (instance == null)
            instance = new Listener();
        return instance;
    }

    private void read(SelectionKey key) {
        responseHandler.handle(key);
    }
}
