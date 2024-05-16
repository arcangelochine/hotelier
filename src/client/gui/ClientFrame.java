package client.gui;

import javax.swing.*;
import java.awt.*;

public class ClientFrame extends JFrame {
    private static ClientFrame instance;

    private static final String TITLE = "Hotelier Client";

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private static final JPanel container = new JPanel();
    private static final AuthPanel authView = AuthPanel.getInstance();
    private static final HotelPanel hotelView = HotelPanel.getInstance();

    private static final CardLayout layout = new CardLayout();

    public static ClientFrame getInstance() {
        if (instance == null)
            instance = new ClientFrame();
        return instance;
    }

    private ClientFrame() {
        super(TITLE);

        // inject dependency
        AuthPanel.setFrame(this);
        HotelPanel.setFrame(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);

        container.setLayout(layout);

        container.add(authView, "auth");
        container.add(hotelView, "hotel");

        // default pane
        layout.show(container, "auth");

        add(container);
        setVisible(true);
    }

    public void viewHotel() {
        layout.show(container, "hotel");
    }
}
