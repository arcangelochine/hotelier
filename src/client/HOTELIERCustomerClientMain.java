package client;

import client.core.Client;
import client.gui.ClientFrame;
import client.util.Logger;

import javax.swing.*;

class HOTELIERCustomerClientMain {
    private static ClientFrame clientFrame;
    private static final Logger logger = Logger.getInstance();
    private static final Client client = Client.getInstance();

    public static void main(String[] args) {
        setup();
        run();

        logger.close();
    }

    private static void setup() {
        client.setup();
    }

    private static void run() {
        SwingUtilities.invokeLater(() -> clientFrame = ClientFrame.getInstance());
        client.run();
    }
}