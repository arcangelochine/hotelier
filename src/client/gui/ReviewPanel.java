package client.gui;

import client.core.Client;
import client.core.RequestHandler;
import client.entities.Hotel;
import client.entities.Review;
import client.entities.User;
import client.protocol.Request;
import client.util.InputWithLabel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ReviewPanel extends JDialog {
    private static final RequestHandler requestHandler = RequestHandler.getInstance();

    InputWithLabel rateInput = InputWithLabel.textInput("Rate", 5);
    HashMap<String, InputWithLabel> ratingInputs = new HashMap<>();

    private final Hotel hotel;
    private final User user;

    public ReviewPanel(Hotel hotel, User user) {
        super();

        this.hotel = hotel;
        this.user = user;

        setModal(true);
        setBounds(0, 0, 600, 400);
        setResizable(false);
        setTitle("Review (" + hotel.getName() + ")");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(rateInput);

        hotel.getRatings().forEach((k, v) -> {
            InputWithLabel ratingField = InputWithLabel.textInput(k, 5);
            ratingInputs.put(k, ratingField);
            add(ratingField);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        buttonPanel.add(cancelButton);

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> addReview());
        buttonPanel.add(okButton);

        add(buttonPanel);
    }

    private void addReview() {
        try {
            Integer id = hotel.getId();
            String username = user.getUsername();
            float rate = Float.parseFloat(rateInput.getText());

            HashMap<String, Float> ratings = new HashMap<>();
            ratingInputs.forEach((k, v) -> ratings.put(k, Float.parseFloat(v.getText())));

            Review review = new Review(id, username, rate, ratings);

            requestHandler.send(Request.push(
                    Client.getInstance().getToken(),
                    "review",
                    review.toJson()
            ));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fields not valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
