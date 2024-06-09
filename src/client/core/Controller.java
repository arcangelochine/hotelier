package client.core;

import client.protocol.Response;
import client.util.Logger;
import client.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static Controller instance;
    private final List<ResponseListener> listeners = new ArrayList<>();

    private static final Logger logger = Logger.getInstance();

    public static Controller getInstance() {
        if (instance == null)
            instance = new Controller();
        return instance;
    }

    public void addResponseListener(ResponseListener listener) {
        listeners.add(listener);
    }

    public void handle(String response) {
        logger.out("Received response: " + response);

        Response res = Response.parse(response);

        if (res.getStatus() != Response.ResponseStatus.OK)
            Utils.errorDialog(res.getContent());

        for (ResponseListener listener : listeners) {
            listener.onResponse(res);
        }
    }
}
