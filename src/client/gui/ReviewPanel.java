package client.gui;

import client.core.HotelManager;
import client.entities.Hotel;
import client.entities.Review;
import client.entities.User;
import client.util.InputWithLabel;
import client.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ReviewPanel extends JDialog {
    private static User user;

    private static final HotelManager hotelManager = HotelManager.getInstance();
    private final InputWithLabel rateInput = InputWithLabel.textInput("Rate", 5);
    private final HashMap<String, InputWithLabel> ratingInputs = new HashMap<>();

    private final Hotel hotel;

    public ReviewPanel(Hotel hotel) {
        super();

        this.hotel = hotel;

        setModal(true);
        setBounds(0, 0, 600, 400);
        setResizable(false);
        setTitle("Review (" + hotel.getName() + ")");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(rateInput);

        hotel.getRatings().forEach((k, v) -> {
            InputWithLabel ratingField = InputWithLabel.textInput(Utils.toTitleCase(k), 5);
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

    public static void setUser(User user) {
        ReviewPanel.user = user;
    }

    private void addReview() {
        if (user == null) {
            Utils.errorDialog("Not logged in.");
            return;
        }

        if (!checkFields()) {
            Utils.errorDialog("Invalid fields.");
            return;
        }

        Integer id = hotel.getId();
        String username = user.getUsername();
        float rate = Float.parseFloat(rateInput.getText());
        HashMap<String, Float> ratings = new HashMap<>();
        ratingInputs.forEach((k, v) -> ratings.put(k, Float.parseFloat(v.getText())));

        Review review = new Review(id, username, rate, ratings);

        if (hotelManager.addReview(review))
            HotelPanel.update();
        else
            Utils.errorDialog("Something went wrong");

        setVisible(false);
    }

    private boolean checkFields() {
        try {
            float rate = Float.parseFloat(rateInput.getText());
            final HashMap<String, Float> ratings = new HashMap<>();
            ratingInputs.forEach((k, v) -> ratings.put(k, Float.parseFloat(v.getText())));

            if (rate < 0 || rate > 5)
                return false;

            for (Float rating : ratings.values())
                if (rating < 0 || rating > 5)
                    return false;

            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
