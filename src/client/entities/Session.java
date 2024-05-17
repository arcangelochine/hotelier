package client.entities;

import java.time.LocalDateTime;
import java.util.List;

public final class Session implements Comparable<Session> {
    private final String token;
    private final String user;
    private LocalDateTime expires;

    public Session(String token, String user) {
        this.token = token;
        this.user = user;
        this.expires = LocalDateTime.now().plusYears(1);
    }

    public Session(String token, String user, LocalDateTime expires) {
        this.token = token;
        this.user = user;
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public String getUser() {
        return user;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    public boolean isExpired() {
        return expires.isBefore(LocalDateTime.now());
    }

    @Override
    public int compareTo(Session session) {
        return session.token.compareTo(token);
    }
}
