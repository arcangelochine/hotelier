package server;

import server.core.Server;
import server.util.Logger;

public final class HOTELIERServerMain {
    private static final Logger logger = Logger.getInstance();
    private static final Server server = Server.getInstance();

    public static void main(String[] args) {
        setup();
        start();
    }

    private static void setup() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
            logger.close();
        }));

        logger.out("Setup completed.");
    }

    private static void start() {
        Server.getInstance().start();
    }
}