package server.database;

import java.util.List;

public final class User implements Comparable<User> {
    public enum Badge {
        NOVICE,
        JUNIOR,
        INTERMEDIATE,
        SENIOR,
        EXPERT;

        public Badge upgrade() {
            if (this == EXPERT)
                return this;

            return Badge.values()[this.ordinal() + 1];
        }

        public Badge downgrade() {
            if (this == NOVICE)
                return this;

            return Badge.values()[this.ordinal() - 1];
        }
    }

    private static final String USER_FILE = "users.json";

    private static final JsonTable<User> table = new UserTable(USER_FILE);

    private final String username;
    private final String password;
    private Badge badge;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.badge = Badge.NOVICE;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Badge getBadge() {
        return badge;
    }

    public Badge upgradeBadge() {
        badge = badge.upgrade();

        return badge;
    }

    public Badge downgradeBadge() {
        badge = badge.downgrade();

        return badge;
    }

    public String toJson() {
        return table.toJson(this);
    }

    public static User fromJson(String obj) {
        return table.fromJson(obj);
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
