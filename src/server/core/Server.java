package server.core;

import server.database.Database;
import server.util.Logger;

import java.io.IOException;

public final class Server {
    private static Server instance;
    private static final Logger logger = Logger.getInstance();
    private static final Listener listener = Listener.getInstance();
    private static final Dispatcher dispatcher = Dispatcher.getInstance();
    private static final Database database = Database.getInstance();

    private volatile boolean running = true;

    public synchronized static Server getInstance() {
        if (instance == null)
            instance = new Server();
        return instance;
    }

    public void shutdown() {
        dispatcher.shutdown();
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public synchronized void setup() {
        try {
            database.commit();
            listener.setup();
            dispatcher.setup();
            logger.out("Server configuration loaded.");
        } catch (IOException ignored) {
            logger.err("Failed to configure the server.");
        }
    }

    public synchronized void run() {
        logger.out("Server listening on port: " + ServerConfiguration.getInstance().getPort());

        while (running)
            listener.listen();

        logger.out("Server shutting down...");
    }
}
