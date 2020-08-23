package com.sps.service;

import com.google.inject.Inject;
import com.sps.entity.Message;
import com.sps.entity.Room;
import com.sps.entity.User;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

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

    public @NotNull Future<List<Message>> getMessages(@NotNull Room room, boolean after, @NotNull LocalDateTime datetime) {
        String sql;
        if (after) {
            sql = "SELECT message.id        id,\n" +
                    "       user.id           user_id,\n" +
                    "       user.username     user_username,\n" +
                    "       message.room      room_id,\n" +
                    "       message.content   content,\n" +
                    "       message.send_time send_time\n" +
                    "FROM message\n" +
                    "         JOIN user on user.id = message.owner\n" +
                    "WHERE room = ?\n" +
                    "  AND send_time > ?\n" +
                    "ORDER BY send_time\n" +
                    "LIMIT 50;\n";
        } else {
            sql = "SELECT message.id        id,\n" +
                    "       user.id           user_id,\n" +
                    "       user.username     user_username,\n" +
                    "       message.room      room_id,\n" +
                    "       message.content   content,\n" +
                    "       message.send_time send_time\n" +
                    "FROM message\n" +
                    "         JOIN user on user.id = message.owner\n" +
                    "WHERE room = ?\n" +
                    "  AND send_time < ?\n" +
                    "ORDER BY send_time\n" +
                    "LIMIT 50;\n";
        }

        Promise<ResultSet> promise = Promise.promise();

        database.getClient().queryWithParams(
                sql,
                new JsonArray().add(room.getId()).add(datetime.toInstant(ZoneOffset.ofHours(8))),
                promise
        );

        return promise.future().map(result ->
                result.getRows().stream()
                        .map(json ->
                                new Message(
                                        json.getInteger("id"),
                                        new User(json.getInteger("user_id"), json.getString("user_username")),
                                        room,
                                        json.getString("content"),
                                        LocalDateTime.ofInstant(json.getInstant("send_time"), ZoneId.of("+8"))
                                )
                        )
                        .collect(Collectors.toList())
        );
    }

}
