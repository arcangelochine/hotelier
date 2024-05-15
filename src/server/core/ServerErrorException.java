package server.core;

public class ServerErrorException extends Exception {
    public ServerErrorException() {
        super("Server error.");
    }
}
