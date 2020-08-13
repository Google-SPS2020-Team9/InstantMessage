package com.sps.connection;

import com.sps.service.ServiceException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class WebSocketRequestContext {

    private final @NotNull JsonObject message;

    private final @NotNull WebSocketConnection connection;

    private final @NotNull WebSocketRoom room;

    private final @NotNull String type;

    private final @NotNull String requestId;

    public WebSocketRequestContext(@NotNull JsonObject message, @NotNull WebSocketConnection connection, @NotNull WebSocketRoom room) {
        this.message = message;
        this.connection = connection;
        this.room = room;

        this.type = message.getString("type");
        this.requestId = message.getString("request_id");

        if (type == null) {
            throw new ServiceException("websocket request should have type");
        }
        if (requestId == null) {
            throw new ServiceException("websocket request should have request_id");
        }
    }

    public @NotNull JsonObject getMessage() {
        return message;
    }

    public @NotNull WebSocketConnection getConnection() {
        return connection;
    }

    public @NotNull WebSocketRoom getRoom() {
        return room;
    }

    public @NotNull String getType() {
        return type;
    }

    public @NotNull String getRequestId() {
        return requestId;
    }

    public void success(@NotNull JsonObject json) {
        json.put("success", true);
        json.put("request_id", requestId);
        json.put("type", type + " result");
        connection.send(json);
    }

    public void fail(@NotNull Throwable throwable) {
        JsonObject json = new JsonObject();
        json.put("success", false);
        json.put("request_id", requestId);
        json.put("type", type + " result");
        json.put("error", throwable.getMessage());
        connection.send(json);
    }
}
