package com.sps.controller;

import com.google.inject.Inject;
import com.sps.connection.WebSocketConnection;
import com.sps.connection.WebSocketRoom;
import com.sps.service.RoomService;
import com.sps.service.ServiceException;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RoomController implements HttpRequestController {

    private final @NotNull RoomService service;
    private final @NotNull WebSocketController websocket;

    private final @NotNull ConcurrentMap<Integer, WebSocketRoom> rooms = new ConcurrentHashMap<>();

    @Inject
    public RoomController(@NotNull RoomService service, @NotNull WebSocketController websocket) {
        this.service = service;
        this.websocket = websocket;
    }

    @Override
    public void init(@NotNull Router router) {
        router.post("/room").handler(wrapper(this::createRoom));
        router.route("/room/:roomId").handler(this::joinRoom);
    }

    private @NotNull Future<Void> createRoom(@NotNull RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        if (body == null) {
            return Future.failedFuture(new ServiceException("body is not json object"));
        }
        String name = body.getString("name");
        if (name == null || name.trim().isBlank()) {
            return Future.failedFuture(new ServiceException("room name is empty"));
        }

        return service.createRoom(name).compose(room -> {
            success(ctx, new JsonObject().put("room", JsonObject.mapFrom(room)));
            return Future.succeededFuture();
        });
    }

    private void joinRoom(@NotNull RoutingContext ctx) {
        ServerWebSocket socket = ctx.request().upgrade();

        Future
                .future((Promise<Integer> promise) -> {
                    int roomId = Integer.parseInt(ctx.request().getParam("roomId"));
                    promise.complete(roomId);
                })
                .compose(this::getWebSocketRoom)
                .onSuccess(room -> {
                    WebSocketConnection connection = new WebSocketConnection(room.getRoom(), socket);
                    websocket.initConnection(connection, room);
                    connection.send(
                            new JsonObject()
                                    .put("success", true)
                                    .put("type", "room state")
                                    .put("room", JsonObject.mapFrom(room.getRoom()))
                    );
                })
                .onFailure(error -> {
                    if (!(error instanceof ServiceException)) {
                        error.printStackTrace();
                    }
                    socket.writeTextMessage(
                            new JsonObject()
                                    .put("success", false)
                                    .put("type", "room state")
                                    .put("error", "Room not exist")
                                    .encode()
                    );
                    socket.close();
                });
    }

    private @NotNull Future<WebSocketRoom> getWebSocketRoom(int roomId) {
        if (rooms.containsKey(roomId)) {
            return Future.succeededFuture(rooms.get(roomId));
        }
        return service.getRoom(roomId).compose(room -> {
            if (room == null) {
                return Future.failedFuture(new ServiceException("room not exist"));
            } else {
                return Future.succeededFuture(room);
            }
        }).map(room -> rooms.computeIfAbsent(roomId, it -> new WebSocketRoom(room)));
    }
}
