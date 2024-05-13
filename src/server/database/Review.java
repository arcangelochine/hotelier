package server.database;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public final class Review {
    private static final String REVIEW_FILE = "reviews.json";

    private static final JsonTable<Review> table = new ReviewTable(REVIEW_FILE);

    private int hotel;
    private String user;
    private LocalDateTime date;
    private float rate;
    private final HashMap<String, Float> ratings = new HashMap<>();

    public int getHotel() {
        return hotel;
    }

    public String getUser() {
        return user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public float getRate() {
        return rate;
    }

    public HashMap<String, Float> getRatings() {
        return ratings;
    }

    public String toJson() {
        return table.toJson(this);
    }

    public static Review fromJson(String obj) {
        return table.fromJson(obj);
    }

    public static List<Review> load() {
        return table.load();
    }

    public static void save(List<Review> reviews) {
        table.save(reviews);
    }

    private static final class ReviewTable extends JsonTable<Review> {

        public ReviewTable(String _fileName) {
            super(_fileName);
        }
    }
}
