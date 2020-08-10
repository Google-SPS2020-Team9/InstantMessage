package com.sps.entity;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class Message {

    private final int id;
    private final @NotNull User owner;
    private final @NotNull Room room;
    private final @NotNull String content;
    private final @NotNull LocalDateTime sendTime;

    public Message(int id, @NotNull User owner, @NotNull Room room, @NotNull String content, @NotNull LocalDateTime sendTime) {
        this.id = id;
        this.owner = owner;
        this.room = room;
        this.content = content;
        this.sendTime = sendTime;
    }

    public int getId() {
        return id;
    }

    public @NotNull User getOwner() {
        return owner;
    }

    public @NotNull Room getRoom() {
        return room;
    }

    public @NotNull String getContent() {
        return content;
    }

    public @NotNull LocalDateTime getSendTime() {
        return sendTime;
    }
}
