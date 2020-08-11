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

    default Handler<RoutingContext> wrapper(@NotNull Function<RoutingContext, Future<Void>> requestHandler) {
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

    default void success(RoutingContext context, JsonObject jsonObject) {
        context.response().end(
                jsonObject
                        .put("success", true)
                        .encode()
        );
    }

    default void fail(RoutingContext context, JsonObject jsonObject, String errorMessage) {
        context.response().end(
                jsonObject
                        .put("success", false)
                        .put("error", errorMessage)
                        .encode()
        );
    }

}
