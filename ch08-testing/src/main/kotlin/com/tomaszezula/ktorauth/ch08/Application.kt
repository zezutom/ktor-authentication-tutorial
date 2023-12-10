package com.tomaszezula.ktorauth.ch08

import com.tomaszezula.ktorauth.ch08.plugins.configureRouting
import com.tomaszezula.ktorauth.ch08.plugins.configureSecurity
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureRouting()
}
