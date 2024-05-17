package client.entities;

import client.util.JsonFactory;

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

    private static final UserFactory json = new UserFactory();

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
        return json.toJson(this);
    }

    public static User parse(String obj) {
        return json.fromJson(obj);
    }

    @Override
    public int compareTo(User user) {
        return user.username.compareTo(username);
    }

    private static final class UserFactory extends JsonFactory<User> {
    }
}
