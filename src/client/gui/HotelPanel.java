package client.gui;

import client.util.InputWithLabel;
import client.util.Utils;

import javax.swing.*;
import java.awt.*;

public class HotelPanel extends JPanel {
    private static HotelPanel instance;

    private static final HotelList hotelList = HotelList.getInstance();

    // fields
    private final InputWithLabel cityField = InputWithLabel.textInput("City", 64);
    private final InputWithLabel nameField = InputWithLabel.textInput("Name", 64);

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
        topBar.add(cityField);
        topBar.add(nameField);
        topBar.add(searchButton);
        topBar.add(UserButton.getInstance());

        add(topBar, BorderLayout.NORTH);
        add(hotelList, BorderLayout.CENTER);
    }

    private void search() {
        String city = cityField.getText();
        String name = nameField.getText();

        if (city == null || city.isEmpty()) {
            Utils.errorDialog("City field is empty!");
            return;
        }

        hotelList.update(city, name);
    }

    public static void update() {
        HotelPanel.getInstance().search();
    }

    private static class TopBar extends JPanel {
        public TopBar() {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        }
    }
}
