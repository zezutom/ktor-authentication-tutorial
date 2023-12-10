package com.tomaszezula.ktorauth.ch03

import com.tomaszezula.ktorauth.ch03.plugins.configureRouting
import com.tomaszezula.ktorauth.ch03.plugins.configureSecurity
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureRouting()
}
