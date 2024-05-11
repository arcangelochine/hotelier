package server;

import server.util.JsonFactory;
import server.util.Logger;
import server.util.Utils;

public final class ServerConfiguration {
    private static final Logger logger = Logger.getInstance();

    private static final ServerConfigurationFactory json = new ServerConfigurationFactory();

    private static final String CONFIG_FILE = "hotelier.config.json";

    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_THREADS = 4;

    private static final int MIN_PORT = 2048;
    private static final int MAX_PORT = 65535;

    private static final int MIN_THREADS = 1;
    private static final int MAX_THREADS = 100;

    private static ServerConfiguration instance;

    private final int port;
    private final int threads;

    private ServerConfiguration() {
        port = DEFAULT_PORT;
        threads = DEFAULT_THREADS;
    }

    private ServerConfiguration(int _port, int _threads) {
        port = Utils.clamp(_port, MIN_PORT, MAX_PORT);
        threads = Utils.clamp(_threads, MIN_THREADS, MAX_THREADS);
    }

    public int getPort() {
        return port;
    }

    public int getThreads() {
        return threads;
    }

    public synchronized static ServerConfiguration getInstance() {
        if (instance == null)
            create();
        return instance;
    }

    private static void create() {
        try {
            ServerConfiguration tempConfig = json.load(CONFIG_FILE);

            instance = new ServerConfiguration(tempConfig.getPort(), tempConfig.getThreads());
            logger.log("Loaded server configuration file: " + CONFIG_FILE);
        } catch (Exception ignored) {
            createDefault();
        }
    }

    private static void createDefault() {
        try {
            instance = new ServerConfiguration();
            json.save(CONFIG_FILE, instance);
            logger.log("Created server configuration file: " + CONFIG_FILE);
        } catch (Exception ignored) {
            logger.err("Could not create server configuration file: " + CONFIG_FILE);
        }
    }

    private static class ServerConfigurationFactory extends JsonFactory<ServerConfiguration> {
    }
}
