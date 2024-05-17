package client.entities;

import com.google.gson.JsonSyntaxException;
import client.util.JsonFactory;

import java.util.HashMap;
import java.util.List;

public final class Hotel {
    private Integer id;
    private String name;
    private String description;
    private String city;
    private String phone;
    private List<String> services;
    private Float rate;
    private final HashMap<String, Float> ratings = new HashMap<>();

    private static final HotelFactory json = new HotelFactory();
    private static final HoteListFactory jsonList = new HoteListFactory();

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getServices() {
        return services;
    }

    public Float getRate() {
        return rate;
    }

    public HashMap<String, Float> getRatings() {
        return ratings;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public void setRatings(HashMap<String, Float> ratings) {
        this.ratings.putAll(ratings);
    }

    public static Hotel parse(String obj) throws JsonSyntaxException {
        return json.fromJson(obj);
    }

    public String toJson() {
        return json.toJson(this);
    }

    public static List<Hotel> parseList(String objList) throws JsonSyntaxException {
        return jsonList.fromJson(objList);
    }

    public static String toJsonList(List<Hotel> list) {
        return jsonList.toJson(list);
    }

    private static class HotelFactory extends JsonFactory<Hotel> {
    }

    private static class HoteListFactory extends JsonFactory<List<Hotel>> {
    }
}
