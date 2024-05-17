package client.gui;

import client.core.ClientConfiguration;
import client.core.RequestHandler;
import client.protocol.Request;
import client.util.InputWithLabel;

import javax.swing.*;
import java.awt.*;

public class HotelPanel extends JPanel {
    private static HotelPanel instance;

    private static final RequestHandler requestHandler = RequestHandler.getInstance();
    private static final HotelList hotelList = HotelList.getInstance();

    // fields
    private final InputWithLabel city = InputWithLabel.textInput("City", 64);
    private final InputWithLabel name = InputWithLabel.textInput("Name", 64);

    public static HotelPanel getInstance() {
        if (instance == null)
            instance = new HotelPanel();
        return instance;
    }

    private HotelPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> search());

        TopBar topBar = new TopBar();
        topBar.add(city);
        topBar.add(name);
        topBar.add(searchButton);
        topBar.add(UserButton.getInstance());

        add(topBar, BorderLayout.NORTH);
        add(hotelList, BorderLayout.CENTER);
    }

    private void search() {
        String request = "hotel/";

        if (city.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "City field is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        request += city.getText();

        if (!name.getText().isEmpty())
            request += "/" + name.getText();

        hotelList.setLastRequest(request);
        requestHandler.send(Request.get(ClientConfiguration.getInstance().getToken(), request));
    }

    private static class TopBar extends JPanel {
        public TopBar() {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        }
    }
}
