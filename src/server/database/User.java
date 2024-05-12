package server.database;

import java.util.List;

public final class User implements Comparable<User> {
    private static final String USER_FILE = "users.json";

    private static final JsonTable<User> table = new UserTable(USER_FILE);

    private final String username;
    private final String password;
    private final String salt;

    public User(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

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

    private static final class UserTable extends JsonTable<User> {

        public UserTable(String _fileName) {
            super(_fileName);
        }
    }
}
