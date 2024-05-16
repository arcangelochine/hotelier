package client.core;

import client.util.JsonFactory;

public class ClientConfiguration {
    private static final ClientConfigurationFactory json = new ClientConfigurationFactory();

    private static final String CONFIG_FILE = "client.config.json";

    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_TOKEN = "";

    private static ClientConfiguration instance;

    private final int port;
    private final String host;
    private final String token;

    private ClientConfiguration() {
        port = DEFAULT_PORT;
        host = DEFAULT_HOST;
        token = DEFAULT_TOKEN;
    }

    private ClientConfiguration(int port, String host, String token) {
        this.port = port;
        this.host = host;
        this.token = token;
    }

    public static ClientConfiguration getInstance() {
        if (instance == null)
            create();
        return instance;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getToken() {
        return token;
    }

    private static void create() {
        try {
            ClientConfiguration config = json.load(CONFIG_FILE);

            instance = new ClientConfiguration(config.getPort(), config.getHost(), config.getToken());
        } catch (Exception ignored) {
            createDefault();
        }
    }

    private static void createDefault() {
        try {
            instance = new ClientConfiguration();
            json.save(CONFIG_FILE, instance);
        } catch (Exception ignored) {
        }
    }

    private static class ClientConfigurationFactory extends JsonFactory<ClientConfiguration> {
    }
}
