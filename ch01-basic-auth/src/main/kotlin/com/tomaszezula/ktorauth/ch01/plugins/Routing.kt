package com.tomaszezula.ktorauth.ch01.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authenticate("auth-basic") {
            get("/") {
                call.respondText("Hello World!")
            }
        }
    }
}
