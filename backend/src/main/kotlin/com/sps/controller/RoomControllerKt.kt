package com.sps.controller

import com.sps.service.RoomServiceKt
import com.sps.service.ServiceException
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

public class RoomControllerKt(private val service: RoomServiceKt) : HttpRequestController {

    override fun init(router: Router) {
    }

    private suspend fun createRoom(ctx: RoutingContext) {
        val body = ctx.bodyAsJson ?: throw ServiceException("body is not json object")

        val name = body.getString("name")
        if (name == null || name.trim().isBlank()) {
            throw ServiceException("room name is empty")
        }

        val room = service.createRoom(name)
        success(ctx, JsonObject().put("room", JsonObject.mapFrom(room)))
    }

}