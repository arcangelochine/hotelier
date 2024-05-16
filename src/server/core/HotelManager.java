package server.core;

import server.database.Database;
import server.database.Hotel;
import server.database.Review;

import java.util.List;
import java.util.stream.Collectors;

public class HotelManager {
    private static HotelManager instance;

    private static final Database database = Database.getInstance();

    public static HotelManager getInstance() {
        if (instance == null)
            instance = new HotelManager();
        return instance;
    }

    // TO-DO: ranking system
    public List<Hotel> getHotels(String city) {
        return database.getHotels().get(city);
    }

    public List<Hotel> getHotels(String city, String name) {
        List<Hotel> hotels = database.getHotels().get(city)
                .stream()
                .filter(hotel -> hotel.getName().equals(name))
                .collect(Collectors.toList());

        if (hotels.isEmpty())
            return null;

        return hotels;
    }

    public List<Review> getReviews(int hotel) {
        return database.getReviews().get(hotel);
    }
}
