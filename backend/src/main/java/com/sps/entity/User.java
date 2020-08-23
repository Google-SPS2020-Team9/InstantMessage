package com.sps.entity;

import org.jetbrains.annotations.NotNull;

public class User {

    private final int id;

    private final @NotNull String name;

    public User(int id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public @NotNull String getName() {
        return name;
    }

}
