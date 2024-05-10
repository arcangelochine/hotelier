package database;

import utility.JsonFactory;
import utility.Logger;

import java.util.List;

public class User implements Comparable<User> {
    private static final String USER_FILE = "users.json";
    private static final Logger logger = Logger.getInstance();

    private static final JsonTable<User> table = new JsonTable<>(USER_FILE, new JsonFactory<>());

    private String username;
    private String password;
    private String salt;

    public static List<User> load() {
        return table.load();
    }

    public static void save(List<User> users) {
        table.save(users);
    }

    @Override
    public int compareTo(User user) {
        return user.username.compareTo(username);
    }
}
