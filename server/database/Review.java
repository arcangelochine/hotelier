package database;

import utility.JsonFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Review {
    private static final String REVIEW_FILE = "reviews.json";

    private static final JsonTable<Review> table = new JsonTable<>(REVIEW_FILE, new JsonFactory<>());

    private int hotel;
    private String user;
    private Date date;
    private float rate;
    private final HashMap<String, Float> ratings = new HashMap<>();
    private String body;

    public static List<Review> load() {
        return table.load();
    }

    public static void save(List<Review> reviews) {
        table.save(reviews);
    }

    public int getHotel() {
        return hotel;
    }
}
