package client.core;

import client.protocol.Response;

public interface ResponseListener {
    void onResponse(Response response);
}
