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
    private final String content;

    private Response(ResponseStatus status, String body, String content) {
        this.status = status;
        this.body = body;
        this.content = content;
    }

    public static Response ok(String body, String content) {
        return new Response(ResponseStatus.OK, body, content);
    }

    public static Response ko(String body, String content) {
        return new Response(ResponseStatus.KO, body, null);
    }

    public static Response notFound(String body) {
        return new Response(ResponseStatus.NOT_FOUND, body, null);
    }

    public static Response bad() {
        return new Response(ResponseStatus.BAD, "Bad request.", null);
    }

    public ResponseStatus getStatus() {
        return status;
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

    public static Response parse(String str) {
        return json.fromJson(str);
    }

    private static final class ResponseFactory extends JsonFactory<Response> {
    }
}
