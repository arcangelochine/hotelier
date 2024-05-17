package client.core;

import client.entities.User;
import client.protocol.Request;
import client.protocol.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class AuthManager implements ResponseListener {
    private static AuthManager instance;

    private static final Client client = Client.getInstance();
    private static final RequestHandler requestHandler = RequestHandler.getInstance();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private Response response;

    private AuthManager() {
    }

    public static AuthManager getInstance() {
        if (instance == null)
            instance = new AuthManager();
        return instance;
    }

    public boolean register(String username, String password) {
        FutureTask<Boolean> task = new FutureTask<>(() -> {
            String body = username + " " + password;
            requestHandler.send(Request.register(body));

            synchronized (AuthManager.this) {
                AuthManager.this.wait();
            }

            if (response == null)
                return false;
            if (response.getStatus() != Response.ResponseStatus.OK)
                return false;
            return response.getBody().equals(body);
        });

        return _login(task);
    }

    public boolean login(String username, String password) {
        FutureTask<Boolean> task = new FutureTask<>(() -> {
            String body = username + " " + password;
            requestHandler.send(Request.login(null, body));

            synchronized (AuthManager.this) {
                AuthManager.this.wait();
            }

            if (response == null)
                return false;
            if (response.getStatus() != Response.ResponseStatus.OK)
                return false;
            return response.getBody().equals(body);
        });

        return _login(task);
    }

    private boolean _login(FutureTask<Boolean> task) {
        executor.execute(task);

        try {
            Boolean result = task.get();
            String token = response.getContent();

            client.setToken(token);

            if (result)
                client.setUser(getUser(token));

            return result;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean logout(String token) {
        FutureTask<Boolean> task = new FutureTask<>(() -> {
            requestHandler.send(Request.logout(token));

            synchronized (AuthManager.this) {
                AuthManager.this.wait();
            }

            if (response == null)
                return false;
            if (response.getStatus() != Response.ResponseStatus.OK)
                return false;
            return response.getBody().equals(token);
        });

        return _logout(task);
    }

    public boolean logoff(String token, User user) {
        FutureTask<Boolean> task = new FutureTask<>(() -> {
            String body = user.getUsername();
            requestHandler.send(Request.logoff(token, body));

            synchronized (AuthManager.this) {
                AuthManager.this.wait();
            }

            if (response == null)
                return false;
            if (response.getStatus() != Response.ResponseStatus.OK)
                return false;
            return response.getBody().equals(body);
        });

        return _logout(task);
    }

    private boolean _logout(FutureTask<Boolean> task) {
        executor.execute(task);

        try {
            Boolean result = task.get();

            if (result) {
                client.setToken(null);
                client.setUser(null);
            }

            return result;
        } catch (Exception ignored) {
            return false;
        }
    }

    private User getUser(String token) {
        FutureTask<User> task = new FutureTask<>(() -> {
            String body = "user/" + token;
            requestHandler.send(Request.get(client.getToken(), body));

            synchronized (AuthManager.this) {
                AuthManager.this.wait();
            }

            if (response == null)
                return null;
            if (response.getStatus() != Response.ResponseStatus.OK)
                return null;
            if (!response.getBody().equals(body))
                return null;

            return User.parse(response.getContent());
        });

        executor.execute(task);

        try {
            return task.get();
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public synchronized void onResponse(Response response) {
        this.response = response;
        this.notifyAll();
    }
}
