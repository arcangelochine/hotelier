package server.core;

import server.database.Database;
import server.database.Hotel;
import server.database.Review;
import server.protocol.Request;
import server.protocol.Response;
import server.util.Logger;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.stream.Collectors;

public final class RequestHandler implements Runnable {
    private static final int BUFFER_SIZE = 1024;

    private static final Logger logger = Logger.getInstance();
    private static final Server server = Server.getInstance();
    private static final Database database = Database.getInstance();
    private static final UserManager userManager = UserManager.getInstance();
    private static final HotelManager hotelManager = HotelManager.getInstance();
    private static final ReviewManager reviewManager = ReviewManager.getInstance();

    private final SelectionKey key;
    private final SocketChannel channel;
    private final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

    private Request request;

    public RequestHandler(SelectionKey key) {
        this.key = key;
        this.channel = (SocketChannel) key.channel();
    }

    @Override
    public void run() {
        if (!Server.getInstance().isRunning())
            close();

        try {
            int bytesRead = channel.read(buffer);

            if (bytesRead == -1)
                close();
            else if (bytesRead > 0) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                handle(new String(bytes).trim());
            }
        } catch (Exception e) {
            // TO-DO: investigate ClosedChannelException
            logger.err(Thread.currentThread().getName() + " failed to read channel: " + e.getMessage());
        }
    }

    public void close() {
        try {
            SocketAddress address = channel.getRemoteAddress();

            key.cancel();
            channel.close();
            logger.out(address + " disconnected.");
        } catch (Exception e) {
            logger.err(Thread.currentThread().getName() + " failed to handle request: " + e.getMessage());
        }
    }

    private void handle(String req) throws IOException {
        try {
            request = Request.parse(req);

            logger.out(Thread.currentThread().getName() + " " + channel.getRemoteAddress() + " -> " + request);

            channel.write(ByteBuffer.wrap((genResponse().toJson() + "\n").getBytes()));
        } catch (Exception e) {
            // TO-DO: investigate AsynchronousCloseException
            logger.out(Thread.currentThread().getName() + " " + channel.getRemoteAddress() + " -> " + req);
            channel.write(ByteBuffer.wrap((Response.bad().toJson() + "\n").getBytes()));
        }
    }

    private Response genResponse() {
        switch (request.getMethod()) {
            case GET:
                return get();
            case PUSH:
                return push();
            case REGISTER:
                return register();
            case LOGIN:
                return login();
            case LOGOUT:
                return logout();
            case LOGOFF:
                return logoff();
            case DEBUG:
                return debug();
            default:
                return Response.ko("Unknown method");
        }
    }

    /**
     * GET hotel city [name] - restituisce una lista di hotel, filtrata in base alla città (primo
     * argomento) e in base al nome (secondo argomento, opzionale).
     * GET user token|name - restituisce l'utente dato il token di sessione o dato il nome.
     * GET badge token|name - restituisce il badge dell'utente dato il token di sessione o dato il nome.
     */
    private Response get() {
        try {
            String[] body = request.getBody().trim().split("/");

            if (body.length < 1)
                return Response.bad();

            switch (body[0].toLowerCase()) {
                case "hotel":
                    return getHotel();
                case "reviews":
                    return getReviews();
                case "user":
                    return getUser();
                case "badge":
                    return getBadge();
                default:
                    return Response.notFound(body[0] + " not found.");
            }
        } catch (Exception e) {
            return Response.ko(e.getMessage());
        }
    }

    // [token] GET hotel/city/name
    private Response getHotel() {
        try {
            String[] body = request.getBody().trim().split("/");

            switch (body.length) {
                case 2:
                    return Response.ok(hotelManager.getHotels(body[1])
                            .stream()
                            .map(Hotel::toJson)
                            .collect(Collectors.joining(",\n", "[", "]")));
                case 3:
                    return Response.ok(hotelManager.getHotels(body[1], body[2])
                            .stream()
                            .map(Hotel::toJson)
                            .collect(Collectors.joining(",\n", "[", "]")));
                default:
                    return Response.bad();
            }
        } catch (Exception e) {
            return Response.notFound("Hotel(s) not found.");
        }
    }

    // [token] GET reviews/hotel
    private Response getReviews() {
        String[] body = request.getBody().trim().split("/");

        if (body.length != 2)
            return Response.bad();

        return Response.ok(hotelManager.getReviews(Integer.parseInt(body[1]))
                .stream()
                .map(Review::toJson)
                .collect(Collectors.joining(",\n", "[", "]")));
    }

    // [token] GET user/token|username
    private Response getUser() {
        try {
            String[] body = request.getBody().trim().split("/");

            if (body.length != 2)
                return Response.bad();

            return Response.ok(userManager.getUser(body[1]).toJson());
        } catch (Exception e) {
            return Response.ko(e.getMessage());
        }
    }

    // [token] GET badge/token|username
    private Response getBadge() {
        try {
            String[] body = request.getBody().trim().split("/");

            if (body.length != 2)
                return Response.bad();

            return Response.ok(userManager.getUser(body[1]).getBadge().toString());
        } catch (Exception e) {
            return Response.ko(e.getMessage());
        }
    }

    private Response push() {
        try {
            if (request.getContent() == null)
                return Response.bad();

            String[] body = request.getBody().trim().split("/");

            switch (body[0].toLowerCase()) {
                case "hotel":
                    return Response.ko("Not implemented yet.");
                case "review":
                    return pushReview();
                default:
                    return Response.bad();
            }
        } catch (Exception e) {
            return Response.ko(e.getMessage());
        }
    }

    // [token] PUSH review { ... }
    private Response pushReview() {
        try {
            return Response.ok(reviewManager.createReview(request.getToken(), request.getContent()));
        } catch (Exception e) {
            return Response.ko(e.getMessage());
        }
    }

    /**
     * REGISTER username password - prova a creare un utente, in caso di successo crea anche una sessione
     * e restituisce il token della sessione.
     */
    private Response register() {
        try {
            String[] body = request.getBody().trim().split(" ");

            if (body.length != 2)
                return Response.bad();

            String username = body[0];
            String password = body[1];

            return Response.ok(userManager.register(username, password));
        } catch (Exception e) {
            return Response.ko(e.getMessage());
        }
    }

    /**
     * LOGIN token - effettua il login di un utente tramite token, in caso di successo restituisce il
     * token della sessione.
     * LOGIN username password - effettua il login di un utente tramite credenziali, in caso di
     * successo crea una sessione e restituisce il token della sessione.
     * Se è presente un token nella richiesta, il body viene ignorato
     */
    private Response login() {
        try {
            String token = request.getToken();

            if (token != null)
                return Response.ok(userManager.login(token).getUsername());

            String[] body = request.getBody().trim().split(" ");

            if (body.length != 2)
                return Response.bad();

            return Response.ok(userManager.login(body[0], body[1]).getToken());
        } catch (Exception e) {
            return Response.ko(e.getMessage());
        }
    }

    /**
     * LOGOUT token - effettua il logout di un utente tramite token, in caso di successo imposta la data
     * di scadenza della sessione al momento del logout
     * Il body della richiesta viene ignorato
     */
    private Response logout() {
        try {
            return Response.ok(userManager.logout(request.getToken()).getToken());
        } catch (Exception e) {
            return Response.ko(e.getMessage());
        }
    }

    /**
     * LOGOFF token - termina tutte le sessioni dell'utente associato al token di sessione
     * LOGOFF username - termina tutte le sessioni dell'utente associato allo username
     */
    private Response logoff() {
        try {
            String token = request.getToken();

            if (token != null)
                return Response.ok(userManager.logoff(token).toJson());

            String[] body = request.getBody().trim().split(" ");

            if (body.length != 1)
                return Response.bad();

            return Response.ok(userManager.logoff(body[0]).toJson());
        } catch (Exception e) {
            return Response.ko(e.getMessage());
        }
    }

    /**
     * DEBUG command - esegue diversi comandi di debug
     */
    private Response debug() {
        switch (request.getBody().toLowerCase()) {
            case "echo":
                return Response.ok("echo");
            case "quit":
                close();
                // TO-DO: ClosedChannelException
                return Response.ok("quit");
            case "commit":
                database.commit();
                return Response.ok("commit");
            case "rollback":
                database.rollback();
                return Response.ok("rollback");
            case "shutdown":
                server.shutdown();
                return Response.ok("shutdown");
            default:
                return Response.ko("Unknown command");
        }
    }
}
