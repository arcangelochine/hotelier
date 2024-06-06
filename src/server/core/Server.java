package server.core;

import server.database.AutoSave;
import server.database.Database;
import server.util.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Server {
    private static Server instance;

    private static final Logger logger = Logger.getInstance();
    private static final ServerConfiguration config = ServerConfiguration.getInstance();
    private static final Database database = Database.getInstance();
    private static final RankManager rankManager = RankManager.getInstance();

    private static final ScheduledExecutorService autoSaver = Executors.newSingleThreadScheduledExecutor();
    private static final ScheduledExecutorService notifier = Executors.newSingleThreadScheduledExecutor();

    private static final Map<Integer, EventHandler> eventHandlers = new HashMap<>();

    private static Selector selector;
    private static ServerSocketChannel serverSocketChannel;

    private static boolean running = true;

    private Server() {
        try {
            setup();
        } catch (IOException e) {
            logger.err("Failed to setup Server");
        }
    }

    public static Server getInstance() {
        if (instance == null)
            instance = new Server();
        return instance;
    }

    public void start() {
        logger.out("Server listening on port: " + config.getPort());

        while (running)
            handleEvents();

        logger.out("Server shutting down...");
    }

    private void setup() throws IOException {
        registerHandler(new AcceptHandler(), SelectionKey.OP_ACCEPT);
        registerHandler(new ReadHandler(), SelectionKey.OP_READ);

        rankManager.update();
        Notifier.setup();

        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(config.getPort()));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        launchThreads();
        logger.out("Server configuration loaded.");
    }

    public void registerHandler(EventHandler eventHandler, Integer op) {
        if (op != SelectionKey.OP_ACCEPT && op != SelectionKey.OP_READ && op != SelectionKey.OP_WRITE && op != SelectionKey.OP_CONNECT)
            throw new IllegalArgumentException("Unknown op: " + op);
        if (eventHandler == null)
            throw new IllegalArgumentException("Event handler cannot be null");

        eventHandlers.put(op, eventHandler);
        logger.log("Registered " + eventHandler.getClass().getSimpleName() + " for op: " + op);
    }

    public void shutdown() {
        try {
            database.commit();
            running = false;

            // shutdown executors
            eventHandlers.values().stream().filter(handler -> handler instanceof ReadHandler).forEach(handler -> ((ReadHandler) handler).shutdown());
            eventHandlers.clear();

            selector.close();
            serverSocketChannel.close();
        } catch (Exception e) {
            logger.err("Error while shutting down the server.");
        }
    }

    /**
     * event loop
     */
    private void handleEvents() {
        try {
            selector.select();

            if (!isRunning())
                return;

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                eventHandlers.get(key.readyOps()).handle(key);
            }
        } catch (IOException e) {
            logger.err("Internal error: " + e.getMessage());
        }
    }

    private void launchThreads() {
        autoSaver.scheduleWithFixedDelay(new AutoSave(), config.getAutoSave(), config.getAutoSave(), TimeUnit.MINUTES);
        notifier.scheduleWithFixedDelay(new Notifier(), 0, 10, TimeUnit.SECONDS);
    }

    public boolean isRunning() {
        return running;
    }
}
