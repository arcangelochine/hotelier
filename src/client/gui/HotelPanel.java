package client.gui;

import client.core.ClientConfiguration;
import client.core.RequestHandler;
import client.protocol.Request;
import client.util.InputWithLabel;

import javax.swing.*;
import java.awt.*;

public class HotelPanel extends JPanel {
    private static HotelPanel instance;
    private static ClientFrame frame;

    private static final RequestHandler requestHandler = RequestHandler.getInstance();

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

        add(topBar, BorderLayout.NORTH);

        HotelList hotels = new HotelList();

        add(hotels, BorderLayout.CENTER);
    }

    private void search() {
        String resource = "hotel/";

        if (city.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "City field is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        resource += city.getText();

        if (!name.getText().isEmpty())
            resource += "/" + name.getText();

        requestHandler.send(Request.get(ClientConfiguration.getInstance().getToken(), resource));
    }

    public static void setFrame(ClientFrame frame) {
        HotelPanel.frame = frame;
    }

    private static class TopBar extends JPanel {
        public TopBar() {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        }
    }

    private static class HotelList extends JPanel {

    }
}
