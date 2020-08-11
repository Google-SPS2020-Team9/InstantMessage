package com.sps.service;

import com.google.inject.Inject;
import com.sps.entity.Room;
import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
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

    public void createRoom(@NotNull String name, @NotNull Handler<AsyncResult<Room>> handle) {
        database.getClient().updateWithParams(
                "INSERT INTO room (name) VALUES (?)",
                new JsonArray().add(name),
                (result) -> {
                    if (result.failed()) {
                        handle.handle(Future.failedFuture(result.cause()));
                    } else {
                        int id = result.result().getKeys().getInteger(0);
                        handle.handle(Future.succeededFuture(new Room(id, name)));
                    }
                }
        );
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

    public Single<Room> rxCreateRoom(@NotNull String name) {
        return database.rxClient().rxUpdateWithParams(
                "INSERT INTO room (name) VALUES (?)",
                new JsonArray().add(name)
        ).map(result -> {
            int id = result.getKeys().getInteger(0);
            return new Room(id, name);
        });
    }


}
