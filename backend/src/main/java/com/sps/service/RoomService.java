package com.sps.service;

import com.google.inject.Inject;
import com.sps.entity.Room;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import org.jetbrains.annotations.NotNull;

public class RoomService {

    private final @NotNull DatabaseService database;

    @Inject
    public RoomService(@NotNull DatabaseService database) {
        this.database = database;
    }

    public void createRoom(@NotNull String name, @NotNull Handler<AsyncResult<Room>> handle) {
        database.getClient().updateWithParams(
                "INSERT INTO room (name) VALUES (?)",
                new JsonArray().add(name),
                (result) -> {
                    if (result.failed()) {
                        handle.handle(Future.failedFuture(result.cause()));
                        return;
                    }
                    int id = result.result().getKeys().getInteger(0);
                    handle.handle(Future.succeededFuture(new Room(id, name)));
                }
        );

    }

}
