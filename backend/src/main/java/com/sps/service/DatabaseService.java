package com.sps.service;

import com.google.inject.Inject;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import org.jetbrains.annotations.NotNull;
import com.sps.service.ServiceModule.DatabaseConfig;

public class DatabaseService {

    private final @NotNull JDBCClient client;
    private final @NotNull JsonObject config;

    @Inject
    public DatabaseService(@NotNull Vertx vertx, @DatabaseConfig @NotNull JsonObject config) {
        this.config = config;
        this.client = JDBCClient.createShared(vertx, config);
    }

    public @NotNull JDBCClient getClient() {
        return client;
    }

    public @NotNull JsonObject getConfig() {
        return config;
    }
}
