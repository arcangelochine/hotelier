package client.protocol;

import client.util.JsonFactory;
import com.google.gson.JsonSyntaxException;

public final class Request {
    public enum RequestMethod {
        GET,
        PUSH,
        REGISTER,
        LOGIN,
        LOGOUT,
        LOGOFF,
        DEBUG
    }

    private static final JsonFactory<Request> json = new RequestFactory();

    private final RequestMethod method;
    private final String token;
    private final String body;
    private final String content;

    private Request(RequestMethod method, String token, String body, String content) {
        this.method = method;
        this.token = token;
        this.body = body;
        this.content = content;
    }

    public static Request get(String token, String body) {
        return new Request(RequestMethod.GET, token, body, null);
    }

    public static Request push(String token, String body, String content) {
        return new Request(RequestMethod.PUSH, token, body, content);
    }

    public static Request register(String body) {
        return new Request(RequestMethod.REGISTER, null, body, null);
    }

    public static Request login(String token, String body) {
        return new Request(RequestMethod.LOGIN, token, body, null);
    }

    public static Request logout(String token) {
        return new Request(RequestMethod.LOGOUT, token, null, null);
    }

    public static Request logoff(String token, String username) {
        return new Request(RequestMethod.LOGOFF, token, username, null);
    }

    public static Request debug(String token, String body) {
        return new Request(RequestMethod.DEBUG, token, body, null);
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getToken() {
        return token;
    }

    public String getBody() {
        return body;
    }

    public String getContent() {
        return content;
    }

    public String toJson() {
        return json.toJson(this);
    }

    public static Request parse(String str) throws JsonSyntaxException {
        return json.fromJson(str);
    }

    @Override
    public String toString() {
        return "[" +
                token +
                "] " +
                method.name() +
                " " +
                body +
                " " +
                content;
    }

    private static final class RequestFactory extends JsonFactory<Request> {
    }
}
