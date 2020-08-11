package com.sps.controller;

import com.google.inject.Inject;
import com.sps.service.RoomService;
import com.sps.service.ServiceException;
import io.reactivex.Completable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
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

    private void createRoom(RoutingContext ctx, Handler<AsyncResult<Void>> handler) {
        JsonObject body = ctx.getBodyAsJson();
        if (body == null) {
            handler.handle(Future.failedFuture(new ServiceException("body is not json object")));
            return;
        }
        String name = body.getString("name");
        if (name == null || name.trim().isBlank()) {
            handler.handle(Future.failedFuture(new ServiceException("room name is empty")));
            return;
        }
        service.createRoom(name, result -> {
            if (result.failed()) {
                handler.handle(Future.failedFuture(result.cause()));
            } else {
                success(ctx, new JsonObject().put("room", JsonObject.mapFrom(result.result())));
                handler.handle(Future.succeededFuture());
            }
        });
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

        return service.createRoom(name).compose(room -> {
            success(ctx, new JsonObject().put("room", JsonObject.mapFrom(room)));
            return Future.succeededFuture();
        });
    }

    private Completable rxCreateRoom(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        if (body == null) {
            return Completable.error(new ServiceException("body is not json object"));
        }
        String name = body.getString("name");
        if (name == null || name.trim().isBlank()) {
            return Completable.error(new ServiceException("room name is empty"));
        }

        return service.rxCreateRoom(name).doOnSuccess(room -> {
            success(ctx, new JsonObject().put("room", JsonObject.mapFrom(room)));
        }).ignoreElement();
    }

    private void joinRoom(RoutingContext ctx) {
        String roomId = ctx.request().getParam("roomId");
    }
}
