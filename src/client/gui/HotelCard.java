package client.gui;

import client.entities.Hotel;
import client.entities.User;
import client.util.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HotelCard extends JPanel {
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private static final Color HOVER_COLOR = new Color(220, 220, 220);
    private static final Border border = BorderFactory.createEmptyBorder(0, 5, 0, 0);

    private static User user;

    private final Hotel hotel;

    public HotelCard(Hotel hotel) {
        this.hotel = hotel;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(DEFAULT_COLOR);

        JLabel nameLabel = new JLabel(hotel.getName());
        JLabel phoneLabel = new JLabel(hotel.getPhone());
        JLabel descriptionLabel = new JLabel(hotel.getDescription());
        JLabel servicesLabel = new JLabel("Services:");
        JLabel rateLabel = new JLabel("Rate: " + String.format("%.2f", hotel.getRate()) + " / 5");
        JLabel ratingsLabel = new JLabel("Ratings:");

        nameLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));
        add(nameLabel);

        descriptionLabel.setBorder(border);
        descriptionLabel.setFont(new Font("Sans Serif", Font.ITALIC, 13));
        add(descriptionLabel);

        phoneLabel.setBorder(border);
        add(phoneLabel);

        add(servicesLabel);
        hotel.getServices().forEach(service -> {
            JLabel serviceLabel = new JLabel(service);
            serviceLabel.setBorder(border);
            add(serviceLabel);
        });

        add(rateLabel);
        add(ratingsLabel);

        hotel.getRatings().forEach((k, v) -> {
            JLabel rating = new JLabel(Utils.toTitleCase(k) + ": " + String.format("%.2f", v) + " / 5");
            rating.setBorder(border);
            add(rating);
        });

        if (user != null) {
            JButton reviewButton = new JButton("Review");
            reviewButton.addActionListener(e -> review());
            add(reviewButton);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(DEFAULT_COLOR);
            }
        });
    }

    public static void setUser(User user) {
        HotelCard.user = user;
    }

    private void review() {
        ReviewPanel reviewPanel = new ReviewPanel(hotel);
        reviewPanel.setVisible(true);
    }
}
