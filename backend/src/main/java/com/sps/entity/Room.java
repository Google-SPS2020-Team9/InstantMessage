package com.sps.entity;

import org.jetbrains.annotations.NotNull;

public class Room {

    private final int id;

    private final @NotNull String name;

    public Room(int id, @NotNull String name) {
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
