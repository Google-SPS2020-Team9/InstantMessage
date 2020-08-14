package com.sps.service;

import com.google.inject.Inject;
import com.sps.entity.Message;
import com.sps.entity.Room;
import com.sps.entity.User;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.UpdateResult;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class MessageService {

    private final @NotNull DatabaseService database;

    @Inject
    public MessageService(@NotNull DatabaseService database) {
        this.database = database;
    }

    public @NotNull Future<Message> createMessage(@NotNull User owner, @NotNull Room room, @NotNull String content) {
        Instant now = Instant.now();

        Promise<UpdateResult> promise = Promise.promise();
        database.getClient().updateWithParams(
                "INSERT INTO message (owner, room, content, send_time) VALUES (?,?,?,?)",
                new JsonArray().add(owner.getId()).add(room.getId()).add(content).add(now),
                promise
        );

        return promise.future().map(result -> {
            int id = result.getKeys().getInteger(0);
            LocalDateTime time = LocalDateTime.ofInstant(now, ZoneId.of("+8"));
            return new Message(id, owner, room, content, time);
        });
    }

}
