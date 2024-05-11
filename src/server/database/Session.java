package server.database;

import java.util.Date;
import java.util.List;

public final class Session implements Comparable<Session> {
    private static final String SESSION_FILE = "sessions.json";

    private static final JsonTable<Session> table = new JsonTable<>(SESSION_FILE);

    private String token;
    private String user;
    private Date expires;

    public static List<Session> load() {
        return table.load();
    }

    public static void save(List<Session> sessions) {
        table.save(sessions);
    }

    @Override
    public int compareTo(Session session) {
        return session.token.compareTo(token);
    }
}
