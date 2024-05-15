package server.core;

import server.database.Database;
import server.database.Session;
import server.database.User;

import java.util.List;
import java.util.stream.Collectors;

import static server.core.SessionManager.TOKEN_LENGTH;

public final class UserManager {
    private static UserManager instance;

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_USERNAME_LENGTH = 30;

    private static final Database database = Database.getInstance();
    private static final SessionManager sessionManager = SessionManager.getInstance();

    public static UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return UserManager.instance;
    }

    private boolean addUser(User user) {
        return database.getUsers().add(user);
    }

    private boolean authUser(String username, String password) {
        return database.getUsers()
                .stream()
                .anyMatch(u -> u.getUsername().equals(username) && u.getPassword().equals(password));
    }

    /**
     * @param username Username desiderato
     * @param password Password desiderata
     * @return il token assegnato dal server
     * @throws UsernameAlreadyExistsException Esiste già un utente con lo username desiderato
     * @throws PasswordTooShortException      La password è più corta di 6 caratteri
     */
    public String register(String username, String password) throws UsernameAlreadyExistsException, PasswordTooShortException, UsernameTooLongException, ServerErrorException {
        if (password.length() < MIN_PASSWORD_LENGTH)
            throw new PasswordTooShortException();

        if (username.length() > MAX_USERNAME_LENGTH)
            throw new UsernameTooLongException();

        User user = new User(username, password);

        if (!addUser(user))
            throw new UsernameAlreadyExistsException();

        return sessionManager.createSession(user.getUsername()).getToken();
    }

    /**
     * @param token Token desiderato
     * @return l'utente associato alla sessione
     * @throws SessionExpiredException La sessione è scaduta
     */
    public User login(String token) throws SessionExpiredException, ServerErrorException, NotFoundException {
        Session session = sessionManager.getSession(token);

        if (session.isExpired())
            throw new SessionExpiredException();

        return getUser(session.getToken());
    }

    /**
     * @param username Username desiderato
     * @param password Password desiderata
     * @return la sessione appena creata
     * @throws WrongCredentialsException Credenziali errate
     */
    public Session login(String username, String password) throws WrongCredentialsException, ServerErrorException {
        if (!authUser(username, password))
            throw new WrongCredentialsException();

        return sessionManager.createSession(username);
    }

    /**
     * @param token Token della sessione da terminare
     * @return la sessione terminata
     * @throws NotLoggedInException    L'utente non è loggato
     * @throws SessionExpiredException La sessione è scaduta
     */
    public Session logout(String token) throws NotLoggedInException, SessionExpiredException, ServerErrorException {
        if (token == null)
            throw new NotLoggedInException();

        Session session = sessionManager.getSession(token);

        if (session.isExpired())
            throw new SessionExpiredException();

        return sessionManager.terminateSession(token);
    }

    public User getUser(String search) throws NotFoundException, SessionExpiredException, ServerErrorException {
        if (search.length() == TOKEN_LENGTH) {
            Session session = SessionManager.getInstance().getSession(search);

            if (session.isExpired())
                throw new SessionExpiredException();

            return getUser(session.getUser());
        }

        return database.getUsers()
                .stream()
                .filter(u -> u.getUsername().equals(search))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    public User logoff(String search) throws ServerErrorException, NotFoundException, SessionExpiredException, NotLoggedInException {
        User user = getUser(search);

        List<String> tokens = database.getSessions()
                .stream()
                .filter(s -> s.getUser().equals(user.getUsername()) && !s.isExpired())
                .map(Session::getToken)
                .collect(Collectors.toList());

        if (tokens.isEmpty())
            throw new NotLoggedInException();

        for (String token : tokens)
            sessionManager.terminateSession(token);

        return user;
    }

    public static final class UsernameAlreadyExistsException extends Exception {
        public UsernameAlreadyExistsException() {
            super("Username already exists.");
        }
    }

    public static final class UsernameTooLongException extends Exception {
        public UsernameTooLongException() {
            super("Username is too long");
        }
    }

    public static final class PasswordTooShortException extends Exception {
        public PasswordTooShortException() {
            super("Password is too short.");
        }
    }

    public static final class SessionExpiredException extends Exception {
        public SessionExpiredException() {
            super("Session expired.");
        }
    }

    public static final class WrongCredentialsException extends Exception {
        public WrongCredentialsException() {
            super("Wrong credentials.");
        }
    }

    public static final class NotLoggedInException extends Exception {
        public NotLoggedInException() {
            super("Not logged in.");
        }
    }
}
