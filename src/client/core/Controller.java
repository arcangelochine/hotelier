package client.core;

import client.protocol.Response;
import client.util.Logger;

import javax.swing.*;

public class Controller {
    private static Controller instance;

    private static final Logger logger = Logger.getInstance();

    public static Controller getInstance() {
        if (instance == null)
            instance = new Controller();
        return instance;
    }

    public void handle(String response) {
        logger.out("Received response: " + response);

        Response res = Response.parse(response);

        if (res.getStatus() == Response.ResponseStatus.OK) {
            // TO-DO: ok
        } else {
            JOptionPane.showMessageDialog(null, res.getBody(), res.getStatus().toString(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
