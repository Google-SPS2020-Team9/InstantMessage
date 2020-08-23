package com.sps.controller;

import com.sps.service.ServiceException;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface HttpRequestController {

    void init(@NotNull Router router);

    default @NotNull Handler<RoutingContext> wrapper(@NotNull Function<RoutingContext, @NotNull Future<Void>> requestHandler) {
        return (ctx) -> {
            requestHandler.apply(ctx).onFailure(error -> {
                if (error instanceof ServiceException) {
                    ctx.response().setStatusCode(400);
                } else {
                    ctx.response().setStatusCode(500);
                    error.printStackTrace();
                }
                fail(ctx, new JsonObject(), error.getMessage());
            });
        };
    }

    default void success(@NotNull RoutingContext context, @NotNull JsonObject jsonObject) {
        context.response().end(
                jsonObject
                        .put("success", true)
                        .encode()
        );
    }

    default void fail(@NotNull RoutingContext context, @NotNull JsonObject jsonObject, @NotNull String errorMessage) {
        context.response().end(
                jsonObject
                        .put("success", false)
                        .put("error", errorMessage)
                        .encode()
        );
    }

}
