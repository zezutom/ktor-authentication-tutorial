package com.tomaszezula.ktorauth.ch01

import com.tomaszezula.ktorauth.ch01.plugins.configureRouting
import com.tomaszezula.ktorauth.ch01.plugins.configureSecurity
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureRouting()
}
