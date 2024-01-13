package com.tomaszezula.ktorauth.ch03

import com.tomaszezula.ktorauth.ch03.plugins.configureRouting
import com.tomaszezula.ktorauth.ch03.plugins.configureSecurity
import com.tomaszezula.ktorauth.ch03.plugins.jwtConfig
import com.tomaszezula.ktorauth.ch03.plugins.loadUsers
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    val jwtConfig = environment.config.config("ktor.auth.jwt").jwtConfig()
    configureSecurity(jwtConfig)
    configureRouting(jwtConfig, loadUsers("users.properties"))
}
