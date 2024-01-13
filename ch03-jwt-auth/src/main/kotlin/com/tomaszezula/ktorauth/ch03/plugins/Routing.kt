package com.tomaszezula.ktorauth.ch03.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC256
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val userRepository = loadUsers("users.properties")
    routing {
        post("/login") {
            val params = call.receiveParameters()
            val username = params["username"]
            val password = params["password"]
            userRepository[username]?.let {
                if (it == password) {
                    // TODO pass params in config
                    val token = JWT.create()
                        .withAudience("ktor-audience")
                        .withIssuer("ktor-issuer")
                        .withClaim("name", username)
                        .sign(HMAC256("ktor-secret"))
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

suspend fun ApplicationCall.loginFailed() {
    respond(HttpStatusCode.Forbidden, "Login failed")
}
fun Application.loadUsers(filePath: String): Map<String, String> {
    val userFile = this.javaClass.classLoader.getResource(filePath)
        ?: throw IllegalArgumentException("Could not read users file: $filePath")
    val delimiter = "="
    userFile.readText().lines()
    return userFile.readText().lines().filter { it.contains(delimiter) }.associate {
        val (name, password) = it.split(delimiter)
        name to password
    }
}