package com.sps.service;

import com.google.inject.Inject;
import com.sps.entity.Room;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.UpdateResult;
import org.jetbrains.annotations.NotNull;

public class RoomService {

    private final @NotNull DatabaseService database;

    @Inject
    public RoomService(@NotNull DatabaseService database) {
        this.database = database;
    }

    public Future<Room> createRoom(@NotNull String name) {
        Promise<UpdateResult> promise = Promise.promise();
        database.getClient().updateWithParams(
                "INSERT INTO room (name) VALUES (?)",
                new JsonArray().add(name),
                promise
        );

        return promise.future().map(result -> {
            int id = result.getKeys().getInteger(0);
            return new Room(id, name);
        });
    }

}
