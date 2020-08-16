package com.sps.controller;

import com.google.inject.Inject;
import com.sps.connection.WebSocketConnection;
import com.sps.connection.WebSocketRequestContext;
import com.sps.connection.WebSocketRoom;
import com.sps.entity.Message;
import com.sps.entity.Room;
import com.sps.entity.User;
import com.sps.service.MessageService;
import com.sps.service.ServiceException;
import com.sps.service.UserService;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class WebSocketController {

    @FunctionalInterface
    interface WebSocketRequestHandler {
        @NotNull Future<Void> handle(@NotNull WebSocketRequestContext context);
    }

    private final @NotNull Map<String, WebSocketRequestHandler> handlers = new HashMap<>();

    private final @NotNull UserService userService;
    private final @NotNull MessageService messageService;

    @Inject
    public WebSocketController(@NotNull UserService userService,
                               @NotNull MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;

        handlers.put("sign in", this::signIn);
        handlers.put("send message", this::sendMessage);
        handlers.put("pull messages", this::pullMessages);
    }

    public void initConnection(@NotNull WebSocketConnection connection,
                               @NotNull WebSocketRoom room) {
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
        if (context.getConnection().getUser() != null) {
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

    private @NotNull Future<Void> sendMessage(@NotNull WebSocketRequestContext context) {
        User user = context.getConnection().getUser();
        Room room = context.getRoom().getRoom();
        if (user == null) {
            return Future.failedFuture(new ServiceException("not login"));
        }

        String content = context.getMessage().getString("content");
        if (content == null || content.isEmpty()) {
            return Future.failedFuture(new ServiceException("content is empty"));
        }

        return messageService.createMessage(user, room, content).compose(message -> {
            context.getRoom().pushMessage(message);
            context.success(
                    new JsonObject().put("message", message.toJsonWithoutRoom())
            );
            return Future.succeededFuture();
        });
    }

    private @NotNull Future<Void> pullMessages(@NotNull WebSocketRequestContext context) {
        User user = context.getConnection().getUser();
        Room room = context.getRoom().getRoom();

        if (user == null) {
            return Future.failedFuture(new ServiceException("not login"));
        }
        String direction = context.getMessage().getString("direction");
        if (direction == null || direction.isEmpty()) {
            return Future.failedFuture(new ServiceException("direction is empty"));
        }
        if (!direction.equals("before") && !direction.equals("after")) {
            return Future.failedFuture(new ServiceException("direction is illegal"));
        }
        boolean after = direction.equals("after");

        String datetimeString = context.getMessage().getString("datetime");
        LocalDateTime dateTime = LocalDateTime.parse(datetimeString);

        return messageService.getMessages(room, after, dateTime).compose(messages -> {
            context.success(
                    new JsonObject().put("messages",
                            messages.stream()
                                    .map(Message::toJsonWithoutRoom)
                                    .collect(Collectors.toList())
                    )
            );
            return Future.succeededFuture();
        });
    }

}
