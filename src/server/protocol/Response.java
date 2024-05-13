package server.protocol;

import server.util.JsonFactory;

public final class Response {
    public enum ResponseStatus {
        OK,
        KO,
        NOT_FOUND,
        BAD
    }

    private static final JsonFactory<Response> json = new ResponseFactory();

    private final ResponseStatus status;
    private final String body;

    private Response(ResponseStatus status, String body) {
        this.status = status;
        this.body = body;
    }

    public static Response ok(String body) {
        return new Response(ResponseStatus.OK, body);
    }

    public static Response ko(String body) {
        return new Response(ResponseStatus.KO, body);
    }

    public static Response notFound(String body) {
        return new Response(ResponseStatus.NOT_FOUND, body);
    }

    public static Response bad() {
        return new Response(ResponseStatus.BAD, "Bad request.");
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }

    public String toJson() {
        return json.toJson(this);
    }

    public static Response parse(String str) {
        return json.fromJson(str);
    }

    private static final class ResponseFactory extends JsonFactory<Response> {
    }
}
