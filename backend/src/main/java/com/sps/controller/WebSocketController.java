package com.sps.controller;

import com.google.inject.Inject;
import com.sps.connection.WebSocketConnection;
import com.sps.connection.WebSocketRequestContext;
import com.sps.connection.WebSocketRoom;
import com.sps.service.ServiceException;
import com.sps.service.UserService;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class WebSocketController {

    @FunctionalInterface
    interface WebSocketRequestHandler {
        @NotNull Future<Void> handle(@NotNull WebSocketRequestContext context);
    }

    private final @NotNull Map<String, WebSocketRequestHandler> handlers = new HashMap<>();

    private final @NotNull UserService userService;

    @Inject
    public WebSocketController(@NotNull UserService userService) {
        this.userService = userService;

        handlers.put("sign in", this::signIn);
    }

    public void initConnection(@NotNull WebSocketConnection connection, @NotNull WebSocketRoom room) {
        connection.getSocket().textMessageHandler(message -> {
            try {
                JsonObject json = new JsonObject(message);
                WebSocketRequestContext context = new WebSocketRequestContext(json, connection, room);
                dispatchRequest(context);
            } catch (Throwable error) {
                connection.send(
                        new JsonObject()
                                .put("success", false)
                                .put("error", error.getMessage())
                                .put("for_request", message)
                );
            }
        });
        connection.getSocket().closeHandler(result -> {
            room.removeConnection(connection);
        });
    }

    private void dispatchRequest(@NotNull WebSocketRequestContext context) {
        WebSocketRequestHandler handler = handlers.get(context.getType());
        if (handler == null) {
            throw new ServiceException("cannot find type for request " + context.getType());
        }
        handler.handle(context).onFailure(context::fail);
    }

    private @NotNull Future<Void> signIn(@NotNull WebSocketRequestContext context) {
        if (context.getConnection().getUser() != null){
            return Future.failedFuture(new ServiceException("already login"));
        }

        String username = context.getMessage().getString("username");
        if (username == null || username.trim().length() < 2) {
            username = "User";
        }

        return userService.createUser(username).compose(user -> {
            context.getConnection().setUser(user);
            context.getRoom().addConnection(context.getConnection());
            context.success(new JsonObject().put("user", JsonObject.mapFrom(user)));
            return Future.succeededFuture();
        });
    }

}
