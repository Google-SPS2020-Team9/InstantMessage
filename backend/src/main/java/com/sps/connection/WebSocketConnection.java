package com.sps.connection;

import com.sps.entity.Room;
import com.sps.entity.User;
import io.vertx.core.http.ServerWebSocket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WebSocketConnection {

    private @Nullable User user;
    private final @NotNull Room room;
    private final @NotNull ServerWebSocket socket;

    public WebSocketConnection(@NotNull Room room, @NotNull ServerWebSocket socket) {
        this.room = room;
        this.socket = socket;
    }

    public @Nullable User getUser() {
        return user;
    }

    public void setUser(@Nullable User user) {
        this.user = user;
    }

    public @NotNull Room getRoom() {
        return room;
    }

    public @NotNull ServerWebSocket getSocket() {
        return socket;
    }
}
