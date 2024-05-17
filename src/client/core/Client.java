package client.core;

import client.entities.User;
import client.gui.HotelCard;
import client.gui.HotelPanel;
import client.gui.UserButton;

public class Client {
    private static Client instance;

    private static final Listener listener = Listener.getInstance();
    private static final Controller controller = Controller.getInstance();
    private static final ClientConfiguration config = ClientConfiguration.getInstance();

    private User user;
    private String token;

    public static Client getInstance() {
        if (instance == null)
            instance = new Client();
        return instance;
    }

    public synchronized void setup() {
        controller.addResponseListener(AuthManager.getInstance());
        controller.addResponseListener(HotelManager.getInstance());
    }

    public void run() {
        listener.listen();
    }

    public synchronized void setUser(User user) {
        this.user = user;

        UserButton.getInstance().setUser(user);
        HotelCard.setUser(user);
        HotelPanel.update();
    }

    public synchronized void setToken(String token) {
        this.token = token;
        config.setToken(token);
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public boolean isLogged() {
        return token != null;
    }
}
