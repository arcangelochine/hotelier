package server.core;

import server.database.Database;
import server.database.Session;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class SessionManager {
    private static SessionManager instance;

    static final int TOKEN_LENGTH = 32;
    private static final Database database = Database.getInstance();

    public static SessionManager getInstance() {
        if (instance == null)
            instance = new SessionManager();
        return instance;
    }

    private char randChar() {
        int rnd = (int) (ThreadLocalRandom.current().nextFloat() * 52);
        char base = (rnd < 26) ? 'A' : 'a';
        return (char) (base + rnd % 26);
    }

    private String genToken() {
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < TOKEN_LENGTH; i++)
            token.append(randChar());

        return token.toString();
    }

    private String genUniqueToken() {
        String token = genToken();

        while (database.getSessions().stream().map(Session::getToken).anyMatch(token::equals))
            token = genToken();

        return token;
    }

    private boolean addSession(Session session) {
        return database.getSessions().add(session);
    }

    public Session createSession(String username) throws ServerErrorException {
        Session session = new Session(genUniqueToken(), username);

        if (!addSession(session))
            throw new ServerErrorException();

        return session;
    }

    public Session getSession(String token) throws ServerErrorException {
        return database.getSessions()
                .stream()
                .filter(s -> s.getToken().equals(token))
                .findFirst()
                .orElseThrow(ServerErrorException::new);
    }

    public boolean isUserOnline(String token) {
        if (token == null || token.isEmpty())
            return false;

        return database.getSessions()
                .stream()
                .anyMatch(s -> s.getToken().equals(token) && !s.isExpired());
    }

    public Session terminateSession(String token) throws ServerErrorException {
        Session session = getSession(token);

        session.setExpires(LocalDateTime.now());

        return session;
    }
}
