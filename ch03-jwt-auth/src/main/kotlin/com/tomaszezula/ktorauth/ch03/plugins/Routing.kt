package com.tomaszezula.ktorauth.ch03.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC256
import com.tomaszezula.ktorauth.ch03.model.JWTConfig
import com.tomaszezula.ktorauth.ch03.model.User
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
            val token = JWT.create()
                .withAudience(jwtConfig.audience)
                .withIssuer(jwtConfig.issuer)
                .withClaim("name", user.name)
                .withClaim("role", user.role)
                .withExpiresAt(clock.instant().plusSeconds(jwtConfig.expirationSeconds))
                .sign(HMAC256(jwtConfig.secret))
            call.respond(mapOf("token" to token))
        }
        authenticate("auth-jwt") {
            get("/") {
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

