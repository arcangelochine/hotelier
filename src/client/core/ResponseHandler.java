package client.core;

import client.util.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ResponseHandler {
    private static ResponseHandler instance;

    private static final int BUFFER_SIZE = 1024 * 10; // non scala bene...
    private static final Logger logger = Logger.getInstance();
    private static final Controller controller = Controller.getInstance();

    private final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

    public static ResponseHandler getInstance() {
        if (instance == null)
            instance = new ResponseHandler();
        return instance;
    }

    public void handle(SelectionKey key) {
        try {
            SocketChannel channel = (SocketChannel) key.channel();
            int bytesRead = channel.read(buffer);

            if (bytesRead == -1)
                close(key);
            else {
                buffer.flip();

                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                handle(new String(bytes).trim());

                buffer.clear();
            }
        } catch (Exception e) {
            logger.err("Failed to read channel: " + e.getMessage());
        }
    }

    private void close(SelectionKey key) {
        try {
            key.cancel();
            SocketChannel channel = (SocketChannel) key.channel();
            channel.close();
        } catch (Exception e) {
            logger.err("Failed to handle server response: " + e.getMessage());
        }
    }

    private void handle(String response) {
        controller.handle(response);
    }
}
