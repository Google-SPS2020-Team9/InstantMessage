package com.sps.service;

import com.google.inject.Inject;
import com.sps.entity.Room;
import com.sps.entity.User;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.UpdateResult;
import org.jetbrains.annotations.NotNull;

public class UserService {

    private final @NotNull DatabaseService database;

    @Inject
    public UserService(@NotNull DatabaseService database) {
        this.database = database;
    }

    public @NotNull Future<User> createUser(@NotNull String username) {
        Promise<UpdateResult> promise = Promise.promise();
        database.getClient().updateWithParams(
                "INSERT INTO user (username) VALUES (?)",
                new JsonArray().add(username),
                promise
        );

        return promise.future().map(result -> {
            int id = result.getKeys().getInteger(0);
            return new User(id, username);
        });
    }

}
