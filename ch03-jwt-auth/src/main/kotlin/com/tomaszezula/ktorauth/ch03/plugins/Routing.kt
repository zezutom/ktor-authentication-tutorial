package com.tomaszezula.ktorauth.ch03.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC256
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.time.Instant

fun Application.configureRouting(jwtConfig: JWTConfig, userRepository: Map<String, String>) {
    routing {
        post("/login") {
            val login = call.receive<Login>()
            userRepository[login.username]?.let {
                if (it == login.password) {
                    val token = JWT.create()
                        .withAudience(jwtConfig.audience)
                        .withIssuer(jwtConfig.issuer)
                        .withClaim("name", login.username)
                        .withExpiresAt(Instant.now().plusSeconds(600))
                        .sign(HMAC256(jwtConfig.secret))
                    call.respond(mapOf("token" to token))
                } else {
                    call.loginFailed()
                }
            } ?: call.loginFailed()

        }
        get("/") {
            call.respondText("Hello World!")
        }
    }
}

@Serializable
data class Login(val username: String, val password: String)

suspend fun ApplicationCall.loginFailed() {
    respond(HttpStatusCode.Forbidden, "Login failed")
}
