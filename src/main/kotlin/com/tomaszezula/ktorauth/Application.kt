package com.tomaszezula.ktorauth

import com.tomaszezula.ktorauth.plugins.configureRouting
import com.tomaszezula.ktorauth.plugins.configureSecurity
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureRouting()
}
