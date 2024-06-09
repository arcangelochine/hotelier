package client.gui;

import client.core.AuthManager;
import client.util.InputWithLabel;
import client.util.Utils;

import javax.swing.*;
import java.awt.*;

public class AuthPanel extends JPanel {
    private static AuthPanel instance;
    private static ClientFrame frame;

    private static final int MAX_USERNAME_LENGTH = 30;
    private static final int MAX_PASSWORD_LENGTH = 32;

    private static final AuthManager authManager = AuthManager.getInstance();

    // fields
    private final InputWithLabel username = InputWithLabel.textInput("Username", MAX_USERNAME_LENGTH);
    private final InputWithLabel password = InputWithLabel.passwordInput("Password", MAX_PASSWORD_LENGTH);

    public static AuthPanel getInstance() {
        if (instance == null)
            instance = new AuthPanel();
        return instance;
    }

    private AuthPanel() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        setBorder(BorderFactory.createEmptyBorder(0, (int) (0.2 * ClientFrame.WIDTH), 0, (int) (0.2 * ClientFrame.WIDTH)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 0, 0);

        // fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        add(username, gbc);

        // password
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(password, gbc);

        // buttons
        gbc.gridwidth = 1;
        gbc.ipady = 20;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 2;
        // buttons
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> register());
        add(registerButton, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        add(loginButton, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.5;
        JButton guestButton = new JButton("Guest");
        guestButton.addActionListener(e -> frame.viewHotel());
        add(guestButton, gbc);
    }

    private void register() {
        if (checkCredentials() && authManager.register(username.getText(), password.getPassword()))
            frame.viewHotel();
    }

    private void login() {
        if (checkCredentials() && authManager.login(username.getText(), password.getPassword()))
            frame.viewHotel();
    }

    private boolean checkCredentials() {
        if (username.getText().isEmpty() || username.getText().length() > MAX_USERNAME_LENGTH) {
            Utils.errorDialog("Username not valid!");
            return false;
        }

        if (password.getPassword().isEmpty() || password.getPassword().length() > MAX_PASSWORD_LENGTH) {
            Utils.errorDialog("Password not valid!");
            return false;
        }

        return true;
    }

    public static void setFrame(ClientFrame frame) {
       AuthPanel.frame = frame;
    }
}
