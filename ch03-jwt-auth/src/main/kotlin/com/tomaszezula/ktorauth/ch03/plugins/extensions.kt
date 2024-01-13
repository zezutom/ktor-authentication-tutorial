package com.tomaszezula.ktorauth.ch03.plugins

import io.ktor.server.application.*
import io.ktor.server.config.*

fun ApplicationConfig.jwtConfig(): JWTConfig {
    return JWTConfig(
        realm = property("realm").getString(),
        secret = property("secret").getString(),
        audience = property("audience").getString(),
        issuer = property("issuer").getString(),
        expirationSeconds = property("expirationSeconds").getString().toInt()
    )
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

data class JWTConfig(
    val realm: String,
    val secret: String,
    val audience: String,
    val issuer: String,
    val expirationSeconds: Int
)