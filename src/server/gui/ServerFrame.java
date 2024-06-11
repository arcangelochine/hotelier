package server.gui;

import javax.swing.*;
import java.awt.*;

public class ServerFrame extends JFrame {
    private static ServerFrame instance;

    private static final String TITLE = "Hotelier Server";

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private static final JPanel container = new JPanel();
    private static final Dashboard dashboard = Dashboard.getInstance();

    private static final CardLayout layout = new CardLayout();

    public static ServerFrame getInstance() {
        if (instance == null)
            instance = new ServerFrame();
        return instance;
    }

    private ServerFrame() {
        super(TITLE);

        // inject dependency
        Dashboard.setFrame(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(true);

        container.setLayout(layout);

        container.add(dashboard, "dashboard");

        layout.show(container, "dashboard");

        add(container);
        setVisible(true);
    }

    public void viewDashboard() {
        layout.show(container, "dashboard");
    }
}
