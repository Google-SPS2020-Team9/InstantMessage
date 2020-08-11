package com.sps.connection;

import com.sps.entity.Room;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class WebSocketRoom {

    private final @NotNull Room room;
    private final @NotNull Set<WebSocketConnection> connections = new HashSet<>();

    public WebSocketRoom(@NotNull Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public void addConnection(@NotNull WebSocketConnection connection) {
        connections.add(connection);
    }

    public void removeConnection(@NotNull WebSocketConnection connection) {
        connections.remove(connection);
    }
}
