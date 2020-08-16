package com.sps.connection;

import com.sps.entity.Message;
import com.sps.entity.Room;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WebSocketRoom {

    private final @NotNull Room room;
    private final @NotNull Set<WebSocketConnection> connections = new ConcurrentHashSet<>();

    public WebSocketRoom(@NotNull Room room) {
        this.room = room;
    }

    public @NotNull Room getRoom() {
        return room;
    }

    public void addConnection(@NotNull WebSocketConnection connection) {
        connections.add(connection);
    }

    public void removeConnection(@NotNull WebSocketConnection connection) {
        connections.remove(connection);
    }

    public void pushMessage(@NotNull Message message) {
        pushMessages(List.of(message));
    }

    public void pushMessages(@NotNull List<Message> messages) {
        JsonObject json = new JsonObject()
                .put("type", "push messages")
                .put("messages",
                        messages.stream()
                                .map(Message::toJsonWithoutRoom)
                                .collect(Collectors.toList())
                );

        for (WebSocketConnection connection : connections) {
            connection.send(json);
        }
    }

}
