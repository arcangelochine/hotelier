package server;

import server.util.Logger;

public final class HOTELIERServerMain {
    private static final String HOTEL_DATABASE = "hotels.json";

    private static final Logger logger = Logger.getInstance();
    private static final Server server = Server.getInstance();

    public static void main(String[] args) {
        setup();
        run();

        // TO-DO: permanent save databases on shutdown and every 5 minutes!!!
        logger.close();
    }

    private static void setup() {
        ServerConfiguration serverConfiguration = ServerConfiguration.getInstance();

        // Hotel.loadHotels(HOTEL_DATABASE);

        server.setup(serverConfiguration);

        logger.log("Setup completed.");
    }

    private static void run() {
        server.run();
    }
}