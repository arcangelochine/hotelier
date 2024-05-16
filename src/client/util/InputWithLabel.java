package client.util;

import javax.swing.*;

public class InputWithLabel extends JPanel {
    private final JTextField textField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    private InputWithLabel(String label, int limit, boolean password) {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel(label));

        textField.setDocument(new JTextFieldLimit(limit));
        passwordField.setDocument(new JTextFieldLimit(limit));

        if (password)
            add(passwordField);
        else
            add(textField);
    }

    public static InputWithLabel textInput(String label, int limit) {
        return new InputWithLabel(label, limit, false);
    }

    public static InputWithLabel passwordInput(String label, int limit) {
        return new InputWithLabel(label, limit, true);
    }

    public String getText() {
        return textField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }
}
