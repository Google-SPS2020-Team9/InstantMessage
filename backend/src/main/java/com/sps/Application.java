package com.sps;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class Application extends AbstractVerticle {

    @Override
    public void start() {

        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route("/echo").handler((RoutingContext ctx) -> {

            ServerWebSocket socket = ctx.request().upgrade();

            socket.textMessageHandler(text -> {
                System.out.println(text);
                socket.writeTextMessage(text);
            });

        });

        server.requestHandler(router).listen(8080);
    }

}
