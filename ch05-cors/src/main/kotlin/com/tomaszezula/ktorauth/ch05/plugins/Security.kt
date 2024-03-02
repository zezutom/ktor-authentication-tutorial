package com.tomaszezula.ktorauth.ch05.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.CORS


fun Application.configureSecurity() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowCredentials = true
        allowNonSimpleContentTypes = true
        allowSameOrigin = true
//        anyHost()  // Adjust this to suit your needs, see below. Don't use in production!
        allowHost("www.example.com", schemes = listOf("http", "https"))
    }
}
