package com.tomaszezula.ktorauth.ch07

import com.tomaszezula.ktorauth.ch07.plugins.configureRouting
import com.tomaszezula.ktorauth.ch07.plugins.configureSecurity
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureRouting()
}
