package com.tomaszezula.ktorauth.ch01.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureSecurity() {
    // The relevant part of application configuration is the following:
    val authConfig = environment.config.config("ktor.auth.basic")

    // The realm is the name of the protected area:
    val realm = authConfig.property("realm").getString()

    // Usernames and passwords are read from the configuration file:
    val usersFile = authConfig.property("usersFile").getString()

    // Load the users from the file. In this example, the file is a simple text file:
    val users = loadUsers(usersFile)

    install(Authentication) {
        basic("auth-basic") {
            this.realm = realm
            validate { credentials ->
                if (credentials.name in users && credentials.password == users[credentials.name]) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
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

