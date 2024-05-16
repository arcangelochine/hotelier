package client.core;

import client.protocol.Request;
import client.util.JsonFactory;
import client.util.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class RequestHandler {
    private static RequestHandler instance;

    private static final Logger logger = Logger.getInstance();
    private static final RequestFactory json = new RequestFactory();

    private static SocketChannel channel;

    private RequestHandler() {}

    public static RequestHandler getInstance() {
        if (instance == null)
            instance = new RequestHandler();
        return instance;
    }

    public void setChannel(SocketChannel channel) {
        RequestHandler.channel = channel;
    }

    public void send(Request request) {
        try {
            channel.write(ByteBuffer.wrap(json.toJson(request).getBytes()));
        } catch (Exception e) {
            logger.err("Could not write to channel: " + e.getMessage());
        }
    }

    private static final class RequestFactory extends JsonFactory<Request> {}
}
