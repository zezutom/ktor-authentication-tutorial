package com.tomaszezula.ktorauth.ch03.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.tomaszezula.ktorauth.ch03.model.JWTConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.time.Clock

fun Application.configureSecurity(jwtConfig: JWTConfig) {
    authentication {
        jwt("auth-jwt") {
            realm = jwtConfig.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtConfig.secret))
                    .withAudience(jwtConfig.audience)
                    .withIssuer(jwtConfig.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtConfig.audience)) JWTPrincipal(credential.payload) else null
            }
            challenge { scheme, realm ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    "Token to access ${HttpHeaders.WWWAuthenticate} $scheme realm=\"$realm\" is either invalid or expired."
                )
            }
        }
    }
}

fun JWTPrincipal.role(): String? = this.payload.getClaim("role").asString()
fun JWTPrincipal.name(): String? = this.payload.getClaim("name").asString()
fun JWTPrincipal.ttl(clock: Clock): Long? = this.payload.expiresAt?.time?.minus(clock.millis())
