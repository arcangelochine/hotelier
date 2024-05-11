package server.database;

import server.util.JsonFactory;

import java.util.List;

public final class User implements Comparable<User> {
    private static final String USER_FILE = "users.json";

    private static final JsonTable<User> table = new UserTable(USER_FILE);

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

    private static final class UserTable extends JsonTable<User> {

        public UserTable(String _fileName) {
            super(_fileName);
        }
    }
}
