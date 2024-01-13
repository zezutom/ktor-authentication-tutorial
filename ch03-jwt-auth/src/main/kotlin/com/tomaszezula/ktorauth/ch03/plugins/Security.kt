package com.tomaszezula.ktorauth.ch03.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    // The relevant part of application configuration is the following:
    val authConfig = environment.config.config("ktor.auth.jwt")

    val jwtRealm = authConfig.property("realm").getString()
    val jwtSecret = authConfig.property("secret").getString()
    val jwtAudience = authConfig.property("audience").getString()
    val jwtDomain = authConfig.property("domain").getString()

    authentication {
        jwt {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
