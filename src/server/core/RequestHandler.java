package server.core;

import server.database.Database;
import server.database.Hotel;
import server.util.Logger;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public final class RequestHandler implements Runnable {
    private static final int BUFFER_SIZE = 1024;

    private static final Logger logger = Logger.getInstance();
    private static final Server server = Server.getInstance();
    private static final Database database = Database.getInstance();

    private final SelectionKey key;
    private final SocketChannel channel;
    private final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

    public RequestHandler(SelectionKey key) {
        this.key = key;
        this.channel = (SocketChannel) key.channel();
    }

    @Override
    public void run() {
        if (!Server.getInstance().isRunning())
            close();

        try {
            int bytesRead = channel.read(buffer);

            if (bytesRead == -1)
                close();
            else if (bytesRead > 0) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                handle(new String(bytes).trim());
            }
        } catch (IOException e) {
            logger.err(Thread.currentThread().getName() + " failed to handle request: " + e.getMessage());
        }
    }

    public void close() {
        try {
            SocketAddress address = channel.getRemoteAddress();

            key.cancel();
            channel.close();
            logger.out(address + " disconnected.");
        } catch (IOException e) {
            logger.err("Failed to close client connection: " + e.getMessage());
        }
    }

    private void handle(String request) {
        try {
            logger.out(Thread.currentThread().getName() + " " +  channel.getRemoteAddress() + " -> " + request);

            // TO-DO: request and response
            if (request.equals("echo"))
                channel.write(ByteBuffer.wrap((request + "\n").getBytes()));
            if (request.equals("list hotels"))
                channel.write(ByteBuffer.wrap(hotels().getBytes()));
            if (request.equals("quit"))
                close();
            if (request.equals("commit"))
                database.commit();
            if (request.equals("rollback"))
                database.rollback();
            if (request.equals("shutdown"))
                server.shutdown();
        } catch (IOException e) {
            logger.err(Thread.currentThread().getName() + " failed to handle request: " + e.getMessage());
        }
    }

    private String hotels() {
        StringBuilder sb = new StringBuilder();

        database.getHotels("Firenze").stream().map(Hotel::getId).forEach(id -> sb.append(id).append("\n"));

        return sb.toString();
    }
}
