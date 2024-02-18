package com.tomaszezula.ktorauth.ch04.plugins

import com.tomaszezula.ktorauth.ch04.model.OAuthConfig
import io.ktor.server.config.*

fun ApplicationConfig.oauthConfig(): OAuthConfig =
    OAuthConfig(
        name = property("name").getString(),
        clientId = property("clientId").getString(),
        clientSecret = property("clientSecret").getString(),
        accessTokenUrl = property("accessTokenUrl").getString(),
        authorizeUrl = property("authorizeUrl").getString()
    )


