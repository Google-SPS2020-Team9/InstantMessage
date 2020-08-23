package com.sps.service;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Scopes;
import com.sps.controller.RoomController;
import com.sps.controller.WebSocketController;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Properties;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class ServiceModule extends AbstractModule {

    @BindingAnnotation
    @Target({FIELD, PARAMETER, METHOD})
    @Retention(RUNTIME)
    @interface DatabaseConfig {
    }

    private final @NotNull Vertx vertx;
    private static final Properties databaseProperties = new Properties();
    private static final String DATABASE_CONFIG_FILE = "databaseConfig.properties";

    public ServiceModule(@NotNull Vertx vertx) {
        this.vertx = vertx;

        try {
            databaseProperties.load(getClass().getClassLoader().getResourceAsStream(DATABASE_CONFIG_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void configure() {
        bind(Vertx.class).toInstance(vertx);
        bind(JsonObject.class).annotatedWith(DatabaseConfig.class).toInstance(
                JsonObject.mapFrom(databaseProperties)
        );
        bind(DatabaseService.class).in(Scopes.SINGLETON);
        bind(RoomService.class).in(Scopes.SINGLETON);
        bind(UserService.class).in(Scopes.SINGLETON);
        bind(MessageService.class).in(Scopes.SINGLETON);

        bind(RoomController.class).in(Scopes.SINGLETON);
        bind(WebSocketController.class).in(Scopes.SINGLETON);
    }
}
