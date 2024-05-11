package server.core;

import server.ServerConfiguration;
import server.util.Logger;

import java.nio.channels.SelectionKey;
import java.util.concurrent.*;

public final class Dispatcher {
    private static Dispatcher instance;
    private static final Logger logger = Logger.getInstance();
    private static final ServerConfiguration config = ServerConfiguration.getInstance();

    private static ExecutorService threadPool;

    private Dispatcher() {}

    public static Dispatcher getInstance() {
        if (instance == null)
            instance = new Dispatcher();
        return instance;
    }

    public void setup() {
        threadPool = new ThreadPoolExecutor(config.getThreads(), config.getThreads(), 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new RequestRejectionHandler());
    }

    public void dispatch(SelectionKey key) {
        threadPool.submit(new RequestHandler(key));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shutdown() {
        threadPool.shutdown();

        try {
            threadPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.err("Internal error: " + e.getMessage());
        }
    }

    private static final class RequestRejectionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (r instanceof RequestHandler) {
                logger.err(Thread.currentThread().getName() + " rejected a request.");
                ((RequestHandler) r).close();
            }
        }
    }
}
