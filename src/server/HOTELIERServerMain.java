package server;

import server.core.Server;
import server.database.Database;
import server.util.Logger;

public final class HOTELIERServerMain {
    private static final Logger logger = Logger.getInstance();
    private static final Server server = Server.getInstance();
    private static final Database database = Database.getInstance();

    public static void main(String[] args) {
        setup();
        run();

        database.commit();
        logger.close();
    }

    private static void setup() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            database.commit();
            logger.close();
        }));

        server.setup();

        logger.log("Setup completed.");
    }

    private static void run() {
        server.run();
    }
}