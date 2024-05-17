package client.entities;

import client.util.JsonFactory;

import java.time.LocalDateTime;
import java.util.HashMap;

public final class Review {
    private final int hotel;
    private final String user;
    private final LocalDateTime date;
    private final float rate;
    private final HashMap<String, Float> ratings = new HashMap<>();

    private static final ReviewFactory json = new ReviewFactory();

    public Review(int hotel, String user, float rate, HashMap<String, Float> ratings) {
        this.hotel = hotel;
        this.user = user;
        this.date = LocalDateTime.now();
        this.rate = rate;
        this.ratings.putAll(ratings);
    }

    public int getHotel() {
        return hotel;
    }

    public String toJson() {
        return json.toJson(this);
    }

    private static final class ReviewFactory extends JsonFactory<Review> {
    }
}
