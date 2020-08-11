package com.sps.controller;

import com.google.inject.Inject;
import com.sps.entity.Room;
import com.sps.service.RoomService;
import com.sps.service.ServiceException;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;

public class RoomController implements HttpRequestController {

    private final @NotNull RoomService service;

    @Inject
    public RoomController(@NotNull RoomService service) {
        this.service = service;
    }

    @Override
    public void init(@NotNull Router router) {
        router.post("/room").handler(wrapper(this::createRoom));
        router.route("/room/:roomId").handler(this::joinRoom);
    }

    private Future<Void> createRoom(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        if (body == null) {
            return Future.failedFuture(new ServiceException("body is not json object"));
        }
        String name = body.getString("name");
        if (name == null || name.trim().isBlank()) {
            return Future.failedFuture(new ServiceException("room name is empty"));
        }

        Promise<Room> promise = Promise.promise();
        service.createRoom(name, promise);
        return promise.future().compose((room -> {
            success(ctx, new JsonObject().put("room", JsonObject.mapFrom(room)));
            return Future.succeededFuture();
        }));
    }

    private void joinRoom(RoutingContext ctx) {
        String roomId = ctx.request().getParam("roomId");
    }
}
