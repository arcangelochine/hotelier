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

    public String getCity() {
        return city;
    }

    public Integer getId() {
        return id;
    }

    private static final class HotelTable extends JsonTable<Hotel> {

        public HotelTable(String _fileName) {
            super(_fileName);
        }
    }
}
