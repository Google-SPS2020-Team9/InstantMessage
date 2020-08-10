package com.sps;

import io.vertx.core.Vertx;

public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Application(), result -> {
            if (result.failed()) {
                result.cause().printStackTrace();
                vertx.close();
            }
        });
    }

}
