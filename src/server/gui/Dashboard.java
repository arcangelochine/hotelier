package server.gui;

import javax.swing.*;

public class Dashboard extends JPanel {
    private static Dashboard instance;
    private static ServerFrame frame;

    public static Dashboard getInstance() {
        if (instance == null)
            instance = new Dashboard();
        return instance;
    }

    private Dashboard() {

    }

    public static void setFrame(ServerFrame frame) {
        Dashboard.frame = frame;
    }
}
