package com.tomaszezula.ktorauth.ch02

import com.tomaszezula.ktorauth.ch02.plugins.configureRouting
import com.tomaszezula.ktorauth.ch02.plugins.configureSecurity
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureRouting()
}
