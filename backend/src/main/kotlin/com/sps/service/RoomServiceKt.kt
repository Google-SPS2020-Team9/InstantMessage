package com.sps.service

import com.google.inject.Inject
import com.sps.entity.Room
import io.vertx.core.json.JsonArray
import io.vertx.kotlin.ext.sql.updateWithParamsAwait

class RoomServiceKt @Inject constructor(private val database: DatabaseService) {

    suspend fun createRoom(name: String): Room {
        val result = database.client.updateWithParamsAwait(
                "INSERT INTO room (name) VALUES (?)",
                JsonArray().add(name)
        )
        val id = result.keys.getInteger(0)
        return Room(id, name)
    }
}