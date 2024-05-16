package client;

import client.core.Client;
import client.gui.ClientFrame;
import client.protocol.Request;
import client.util.Logger;

import javax.swing.*;

class HOTELIERCustomerClientMain {
    private static ClientFrame clientFrame;
    private static final Request req = Request.get("asd", "asd");
    private static final Logger logger = Logger.getInstance();
    private static final Client client = Client.getInstance();

    public static void main(String[] args) {
        setup();
        run();
    }

    private static void setup() {
        client.setup();
        // TO-DO: save token to skip login
    }

    private static void run() {
        SwingUtilities.invokeLater(() -> clientFrame = ClientFrame.getInstance());
        client.run();
    }
}