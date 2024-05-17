package server;

import server.core.Server;
import server.database.Database;
import server.util.Logger;

public final class HOTELIERServerMain {
    private static final Logger logger = Logger.getInstance();
    private static final Server server = Server.getInstance();

    public static void main(String[] args) {
        setup();
        run();

        Database.getInstance().commit();
        logger.close();
    }

    private static void setup() {
        server.setup();

        logger.log("Setup completed.");
    }

    private static void run() {
        server.run();
    }
}