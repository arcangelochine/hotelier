package server.core;

import server.database.AutoSave;
import server.database.Database;
import server.util.Logger;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Server {
    private static Server instance;
    private static final Logger logger = Logger.getInstance();
    private static final ServerConfiguration config = ServerConfiguration.getInstance();
    private static final Listener listener = Listener.getInstance();
    private static final Dispatcher dispatcher = Dispatcher.getInstance();
    private static final Database database = Database.getInstance();
    private static final RankManager rankManager = RankManager.getInstance();
    private static final ScheduledExecutorService autoSaver = Executors.newSingleThreadScheduledExecutor();
    private static final ScheduledExecutorService notifier = Executors.newSingleThreadScheduledExecutor();

    private volatile boolean running = true;

    public static Server getInstance() {
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
            rankManager.update();
            database.commit();
            listener.setup();
            dispatcher.setup();
            Notifier.setup();
            logger.out("Server configuration loaded.");
        } catch (IOException ignored) {
            logger.err("Failed to configure the server.");
        }
    }

    public synchronized void run() {
        logger.out("Server listening on port: " + config.getPort());

        launchThreads();

        while (running)
            listener.listen();

        logger.out("Server shutting down...");
    }

    private void launchThreads() {
        autoSaver.scheduleWithFixedDelay(new AutoSave(), config.getAutoSave(), config.getAutoSave(), TimeUnit.MINUTES);
        notifier.scheduleWithFixedDelay(new Notifier(), 0, 10, TimeUnit.SECONDS);
    }
}
