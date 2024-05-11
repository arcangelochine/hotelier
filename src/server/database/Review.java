package server.database;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public final class Review {
    private static final String REVIEW_FILE = "reviews.json";

    private static final JsonTable<Review> table = new ReviewTable(REVIEW_FILE);

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

    private static final class ReviewTable extends JsonTable<Review> {

        public ReviewTable(String _fileName) {
            super(_fileName);
        }
    }
}
