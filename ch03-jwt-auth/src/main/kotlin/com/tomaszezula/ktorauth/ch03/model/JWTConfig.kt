package com.tomaszezula.ktorauth.ch03.model

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.time.Clock

data class JWTConfig(
    val realm: String,
    val secret: String,
    val audience: String,
    val issuer: String,
    val expirationSeconds: ExpirationSecondsConfig
) {
    data class ExpirationSecondsConfig(val accessToken: Long, val refreshToken: Long)
}

fun JWTConfig.createToken(clock: Clock, user: User, expirationSeconds: Long): String =
    JWT.create()
        .withAudience(this.audience)
        .withIssuer(this.issuer)
        .withClaim("name", user.name)
        .withClaim("role", user.role)
        .withExpiresAt(clock.instant().plusSeconds(expirationSeconds))
        .sign(Algorithm.HMAC256(this.secret))

fun JWTConfig.verify(token: String): User? =
    try {
        val jwt = JWT.require(Algorithm.HMAC256(this.secret))
            .withAudience(this.audience)
            .withIssuer(this.issuer)
            .build()
            .verify(token)
        jwt.getClaim("name").asString()?.let { name ->
            jwt.getClaim("role").asString()?.let { role ->
                User(name, role)
            }
        }
    } catch (e: JWTVerificationException) {
        null
    }