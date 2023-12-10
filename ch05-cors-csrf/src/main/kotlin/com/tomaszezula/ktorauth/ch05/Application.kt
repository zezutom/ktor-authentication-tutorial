package com.tomaszezula.ktorauth.ch05

import com.tomaszezula.ktorauth.ch05.plugins.configureRouting
import com.tomaszezula.ktorauth.ch05.plugins.configureSecurity
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureRouting()
}
