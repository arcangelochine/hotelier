package server.database;

import java.util.HashMap;
import java.util.List;

public final class Hotel {
    private static final String HOTEL_FILE = "hotels.json";

    private static final JsonTable<Hotel> table = new HotelTable(HOTEL_FILE);

    private Integer id;
    private String name;
    private String description;
    private String city;
    private String phone;
    private List<String> services;
    private Float rate;
    private final HashMap<String, Float> ratings = new HashMap<>();

    public static List<Hotel> load() {
        return table.load();
    }

    public static void save(List<Hotel> hotels) {
        table.save(hotels);
    }

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

    public String toJson() {
        return table.toJson(this);
    }

    public static Hotel fromJson(String obj) {
        return table.fromJson(obj);
    }

    private static final class HotelTable extends JsonTable<Hotel> {

        public HotelTable(String _fileName) {
            super(_fileName);
        }
    }
}
