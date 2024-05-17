package client.gui;

import client.core.HotelManager;

import javax.swing.*;

import java.awt.*;

public class HotelList extends JPanel {
    private static HotelList instance;
    private final JPanel contentPanel;

    private static final HotelManager hotelManager = HotelManager.getInstance();

    private HotelList() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 1)); // Use GridLayout with 1 column and auto rows
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);
    }

    public static HotelList getInstance() {
        if (instance == null)
            instance = new HotelList();
        return instance;
    }

    public void update(String city, String name) {
        contentPanel.removeAll();
        hotelManager.getHotels(city, name).forEach(hotel -> contentPanel.add(new HotelCard(hotel)));
        contentPanel.revalidate();
    }
}
