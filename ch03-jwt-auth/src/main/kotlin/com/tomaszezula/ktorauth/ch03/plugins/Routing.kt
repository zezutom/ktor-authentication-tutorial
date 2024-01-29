package com.tomaszezula.ktorauth.ch03.plugins

import com.tomaszezula.ktorauth.ch03.model.JWTConfig
import com.tomaszezula.ktorauth.ch03.model.User
import com.tomaszezula.ktorauth.ch03.model.createToken
import com.tomaszezula.ktorauth.ch03.model.verify
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.time.Clock

fun Application.configureRouting(jwtConfig: JWTConfig, userRepository: List<User>, clock: Clock) {
    routing {
        post("/login") {
            val login = call.receive<Login>()
            val user = userRepository.firstOrNull {
                it.name == login.username && it.password == login.password
            } ?: run {
                call.respond(HttpStatusCode.Forbidden, "Login failed")
                return@post
            }

            fun createToken(expirationSeconds: Long): String =
                jwtConfig.createToken(clock, user, expirationSeconds)

            val accessToken = createToken(jwtConfig.expirationSeconds.accessToken)
            val refreshToken = createToken(jwtConfig.expirationSeconds.refreshToken)
            call.respond(
                mapOf(
                    "accessToken" to accessToken,
                    "refreshToken" to refreshToken
                )
            )
        }
        post("/refresh") {
            // Extract the refresh token from the request
            val refreshToken = call.receive<RefreshToken>()

            // Verify the refresh token and obtain the user
            val user = jwtConfig.verify(refreshToken.token) ?: run {
                call.respond(HttpStatusCode.Forbidden, "Invalid refresh token")
                return@post
            }

            // Create new access and refresh tokens for the user
            val newAccessToken = jwtConfig.createToken(clock, user, jwtConfig.expirationSeconds.accessToken)
            val newRefreshToken = jwtConfig.createToken(clock, user, jwtConfig.expirationSeconds.refreshToken)

            // Respond with the new tokens
            call.respond(
                mapOf(
                    "accessToken" to newAccessToken,
                    "refreshToken" to newRefreshToken
                )
            )
        }
        authenticate("auth-jwt") {
            get("/me") {
                val principal = call.principal<JWTPrincipal>()
                call.respondText("Hello ${principal?.name()}! Your token expires in ${principal?.ttl(clock)} ms.")
            }
            get("/admin") {
                val principal = call.principal<JWTPrincipal>()
                if (principal?.role() != "admin") {
                    call.respond(HttpStatusCode.Forbidden, "You are not authorized to access this resource!")
                    return@get
                }
                call.respondText("Hello admin ${principal.name()}! Your token expires in ${principal.ttl(clock)} ms.")
            }
        }
    }
}


@Serializable
data class Login(val username: String, val password: String)

@Serializable
data class RefreshToken(val token: String)

