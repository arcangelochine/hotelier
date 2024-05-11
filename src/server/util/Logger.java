package server.util;

import java.io.PrintWriter;
import java.time.LocalDateTime;

public final class Logger implements AutoCloseable {
    private static final String LOG_FILE = "hotelier.log";

    private volatile static Logger instance;
    private static PrintWriter writer;

    private Logger() throws Exception {
        writer = new PrintWriter(LOG_FILE);
    }

    public synchronized static Logger getInstance() {
        if (instance == null)
            create();
        return instance;
    }

    private static void create() {
        try {
            instance = new Logger();
        } catch (Exception ignored) {
            System.err.println("Could not open log file: " + LOG_FILE);
        }
    }

    /**
     * La documentazione non specifica se PrintWriter è thread-safe o meno,
     * nonostante estenda Writer che è thread-safe
     */
    public synchronized void log(String message) {
        if (writer != null)
            writer.println("[" + LocalDateTime.now() + "] " + message);
    }

    public void out(String message) {
        System.out.println("[" + LocalDateTime.now() + "] " + message);
        log(message);
    }

    public void err(String message) {
        System.err.println("[" + LocalDateTime.now() + "] " + message);
        log(message);
    }

    @Override
    public void close() {
        writer.close();
    }
}
