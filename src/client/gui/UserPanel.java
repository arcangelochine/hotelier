package client.gui;

import client.core.AuthManager;
import client.core.Client;
import client.entities.User;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JDialog {
    private final User user;

    private static final AuthManager authManager = AuthManager.getInstance();
    private static final Client client = Client.getInstance();
    private static ClientFrame frame;

    public UserPanel(User user) {
        super();

        if (user == null)
            throw new IllegalArgumentException();

        this.user = user;

        setModal(true);
        setBounds(50, 50, 400, 200);
        setResizable(false);
        setTitle("User Info");
        setLayout(new BorderLayout());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(new JLabel("Name: " + user.getUsername()));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(new JLabel("Badge: " + user.getBadge()));
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton logoff = new JButton("Logoff");
        logoff.addActionListener(e -> logoff());
        buttonPanel.add(logoff);

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> logout());
        buttonPanel.add(logout);

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> setVisible(false));
        buttonPanel.add(okButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void logoff() {
        setVisible(false);

        if (authManager.logoff(client.getToken(), user))
            frame.viewAuth();
    }

    private void logout() {
        setVisible(false);

        if (authManager.logout(client.getToken()))
            frame.viewAuth();
    }

    public static void setFrame(ClientFrame frame) {
        UserPanel.frame = frame;
    }
}
