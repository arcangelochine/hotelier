package client.gui;

import javax.swing.*;
import java.awt.*;

public class Notification extends JDialog {
    public Notification(String message) {
        super();

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
        centerPanel.add(new JLabel(message));
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> setVisible(false));
        buttonPanel.add(okButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}
