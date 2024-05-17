package client.gui;

import client.entities.User;

import javax.swing.*;

public class UserButton extends JButton {
    private static UserButton instance;

    private static ClientFrame frame;

    private User user;

    private UserButton() {
        super();

        setUser(user);

        addActionListener(e -> onClick());
    }

    public static UserButton getInstance() {
        if (instance == null)
            instance = new UserButton();
        return instance;
    }

    public void setUser(User user) {
        this.user = user;

        setText(user == null ? "Guest" : user.getUsername());
    }

    private void onClick() {
        if (user == null)
            frame.viewAuth();
        else
            userDialog();
    }

    private void userDialog() {
        UserPanel userPanel = new UserPanel(user);
        userPanel.setVisible(true);
    }

    public static void setFrame(ClientFrame frame) {
        UserButton.frame = frame;
    }
}
