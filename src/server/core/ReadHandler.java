package server.core;

import server.util.Logger;

import java.nio.channels.SelectionKey;
import java.util.concurrent.*;

public final class ReadHandler implements EventHandler {
    private static final Logger logger = Logger.getInstance();
    private static final ServerConfiguration config = ServerConfiguration.getInstance();
    private static final ExecutorService executor = new ThreadPoolExecutor(config.getThreads(), config.getThreads(), 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new RequestRejectionHandler());

    @Override
    public void handle(SelectionKey key) {
        key.interestOps(0); // blocco la SelectionKey fino al completamento della gestione della richiesta
        executor.submit(new RequestHandler(key));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shutdown() {
        executor.shutdown();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.err("Internal error: " + e.getMessage());
        }
    }

    private static final class RequestRejectionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (r instanceof RequestHandler) {
                logger.err("Rejected a request."); // server at capacity
                ((RequestHandler) r).close(); // close client channel
            }
        }
    }
}
