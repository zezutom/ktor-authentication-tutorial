package com.tomaszezula.ktorauth.ch02.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

fun Application.configureSecurity() {
    // The relevant part of application configuration is the following:
    val authConfig = environment.config.config("ktor.auth.form")

    // Usernames and passwords are read from the configuration file:
    val usersFile = authConfig.property("usersFile").getString()

    // Load the users from the file. In this example, the file is a simple text file:
    val users = loadUsers(usersFile)

    install(Authentication) {
        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                if (credentials.name in users && credentials.password == users[credentials.name]) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("/login")
            }
        }
        session<UserSession>("auth-session") {
            validate { session ->
                if (session.name in users) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("/login")
            }
        }
    }
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60
        }
    }
}

data class UserSession(val name: String) : Principal

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


