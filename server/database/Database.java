package database;

import utility.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public final class Database {
    private static Database instance;

    private static final Logger logger = Logger.getInstance();

    // Hotel indicizzati in base alla città
    private final ConcurrentHashMap<String, List<Hotel>> hotels = new ConcurrentHashMap<>();

    // Recensioni indicizzate in base all'hotel
    private final ConcurrentHashMap<Integer, List<Review>> reviews = new ConcurrentHashMap<>();

    // Utenti ordinati per username
    private final ConcurrentSkipListSet<User> users = new ConcurrentSkipListSet<>();

    // Sessioni ordinate per token
    private final ConcurrentSkipListSet<Session> sessions = new ConcurrentSkipListSet<>();

    private Database() {
        hotels.putAll(Hotel.load().stream().collect(Collectors.groupingBy(Hotel::getCity)));
        reviews.putAll(Review.load().stream().collect(Collectors.groupingBy(Review::getHotel)));
        users.addAll(User.load());
        sessions.addAll(Session.load());
    }

    public synchronized static Database getInstance() {
        if (instance == null)
            create();
        return instance;
    }

    private static void create() {
        instance = new Database();
        logger.out("Database created.");
    }

    /**
     * Rende permanenti le modifiche sul database.
     * Se fallisce la scrittura di una tabella non è garantita la consistenza dei dati.
     */
    public synchronized void commit() {
        Hotel.save(hotels.values()
                .stream()
                .flatMap(List::stream)
                .sorted(Comparator.comparing(Hotel::getId))
                .collect(Collectors.toList()));

        Review.save(reviews.values()
                .stream()
                .flatMap(List::stream)
                .sorted(Comparator.comparing(Review::getHotel))
                .collect(Collectors.toList()));

        User.save(new ArrayList<>(users));

        Session.save(new ArrayList<>(sessions));
    }

    /**
     * Ripristina lo stato più recente del database.
     */
    public synchronized void rollback() {
        hotels.clear();
        reviews.clear();
        users.clear();
        sessions.clear();

        hotels.putAll(Hotel.load().stream().collect(Collectors.groupingBy(Hotel::getCity)));
        reviews.putAll(Review.load().stream().collect(Collectors.groupingBy(Review::getHotel)));
        users.addAll(User.load());
        sessions.addAll(Session.load());
    }
}
