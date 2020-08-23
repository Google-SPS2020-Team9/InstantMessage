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

    public @NotNull Future<Room> createRoom(@NotNull String name) {
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

    public @NotNull Future<Room> getRoom(int id) {
        Promise<JsonArray> promise = Promise.promise();
        database.getClient().querySingleWithParams(
                "SELECT id, name FROM room WHERE id = ?",
                new JsonArray().add(id),
                promise
        );

        return promise.future().map(result -> {
            if (result == null) {
                return null;
            } else {
                return new Room(result.getInteger(0), result.getString(1));
            }
        });
    }

}
