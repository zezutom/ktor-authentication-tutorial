package com.tomaszezula.ktorauth.ch04

import com.tomaszezula.ktorauth.ch04.model.JWTConfig
import com.tomaszezula.ktorauth.ch04.model.OAuthConfig
import com.tomaszezula.ktorauth.ch04.plugins.configureRouting
import com.tomaszezula.ktorauth.ch04.plugins.configureSecurity
import com.tomaszezula.ktorauth.ch04.plugins.jwtConfig
import com.tomaszezula.ktorauth.ch04.plugins.oauthConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import java.time.Clock

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val jwtConfig = environment.config.config("ktor.auth.jwt").jwtConfig()
    val googleConfig = environment.config.config("ktor.auth.oauth.google").oauthConfig()
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
    configureSecurityAndRouting(jwtConfig, googleConfig, httpClient)
}

fun Application.configureSecurityAndRouting(
    jwtConfig: JWTConfig,
    oauthConfig: OAuthConfig,
    httpClient: HttpClient
) {
    configureSecurity(jwtConfig, oauthConfig, httpClient)
    configureRouting(jwtConfig, oauthConfig, httpClient, Clock.systemUTC())
}