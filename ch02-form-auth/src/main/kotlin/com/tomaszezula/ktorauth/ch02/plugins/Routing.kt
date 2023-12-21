package com.tomaszezula.ktorauth.ch02.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*

fun Application.configureRouting() {
    routing {
        authenticate("auth-session", "auth-form") {
            get("/") {
                call.respondText("Hello, ${call.principal<UserSession>()?.name}!")
            }
            post("/login") {
                val username = call.principal<UserIdPrincipal>()?.name
                username?.let { call.sessions.set(UserSession(it)) }
                call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}!")
            }
        }
        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/login")
        }
        get("/login") {
            call.respondHtml {
                body {
                    form(
                        action = "/login",
                        encType = FormEncType.applicationXWwwFormUrlEncoded,
                        method = FormMethod.post
                    ) {
                        p {
                            +"Username:"
                            textInput(name = "username")
                        }
                        p {
                            +"Password:"
                            passwordInput(name = "password")
                        }
                        p {
                            submitInput() { value = "Login" }
                        }
                    }
                }
            }
        }
    }
}
