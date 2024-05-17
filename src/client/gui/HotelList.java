package client.gui;

import client.core.ResponseListener;
import client.entities.Hotel;
import client.protocol.Response;

import javax.swing.*;
import java.util.List;

import java.awt.*;

public class HotelList extends JPanel implements ResponseListener {
    private static HotelList instance;
    private final JPanel contentPanel;

    private String lastRequest;

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

    public void setLastRequest(String lastRequest) {
        this.lastRequest = lastRequest;
    }

    @Override
    public void onResponse(Response response) {
        if (response.getStatus() != Response.ResponseStatus.OK)
            return;

        if (!response.getBody().equals(lastRequest))
            return;

        String content = response.getContent();
        contentPanel.removeAll();

        try {
            List<Hotel> hotels = Hotel.parseList(content);

            // hotels.forEach(hotel -> contentPanel.add(new HotelCard(hotel)));
        } catch (Exception ignored) {
            // ignore non-hotel responses
        }

        contentPanel.revalidate();
    }
}
