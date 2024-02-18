package com.tomaszezula.ktorauth.ch04

import com.tomaszezula.ktorauth.ch04.model.UserSession
import com.tomaszezula.ktorauth.ch04.plugins.configureSecurity
import com.tomaszezula.ktorauth.ch04.plugins.configureRouting
import com.tomaszezula.ktorauth.ch04.plugins.oauthConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.sessions.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Sessions) {
        cookie<UserSession>("user_session")
    }
    val oauthName = "google-oauth"
    val googleConfig = environment.config.config("ktor.auth.oauth.google").oauthConfig()
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
    val redirects = mutableMapOf<String, String>()
    configureSecurity(oauthName, googleConfig, httpClient, redirects)
    configureRouting(oauthName, redirects)
}
