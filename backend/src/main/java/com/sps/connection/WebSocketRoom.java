package com.sps.connection;

import com.sps.entity.Room;
import io.vertx.core.impl.ConcurrentHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

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

}
