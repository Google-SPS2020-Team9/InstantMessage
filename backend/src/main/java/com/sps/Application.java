package com.sps;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sps.controller.HttpRequestController;
import com.sps.controller.RoomController;
import com.sps.service.ServiceModule;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class Application extends AbstractVerticle {

    @Override
    public void start() {

        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route().handler(
                CorsHandler.create("*")
                        .allowedHeader("Access-Control-Allow-Method")
                        .allowedHeader("Access-Control-Allow-Origin")
                        .allowedHeader("Content-Type")
                        .allowedMethod(HttpMethod.GET)
                        .allowedMethod(HttpMethod.POST)
                        .allowedMethod(HttpMethod.HEAD)
                        .allowedMethod(HttpMethod.OPTIONS)
                        .allowedMethod(HttpMethod.DELETE)
        );
        router.route().handler(BodyHandler.create());

        Injector injector = Guice.createInjector(new ServiceModule(vertx));
        HttpRequestController[] controllers = new HttpRequestController[]{
                injector.getInstance(RoomController.class)
        };
        for (HttpRequestController controller : controllers) {
            controller.init(router);
        }

        server.requestHandler(router).listen(8080);
    }

}
