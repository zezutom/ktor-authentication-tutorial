package com.tomaszezula.ktorauth.ch03.plugins

import com.tomaszezula.ktorauth.ch03.model.JWTConfig
import com.tomaszezula.ktorauth.ch03.model.User
import io.ktor.server.application.*
import io.ktor.server.config.*

fun ApplicationConfig.jwtConfig(): JWTConfig {
    return JWTConfig(
        realm = property("realm").getString(),
        secret = property("secret").getString(),
        audience = property("audience").getString(),
        issuer = property("issuer").getString(),
        expirationSeconds = property("expirationSeconds").getString().toLong()
    )
}

fun Application.loadUsers(filePath: String): List<User> {
    val userFile = this.javaClass.classLoader.getResource(filePath)
        ?: throw IllegalArgumentException("Could not read users file: $filePath")
    val delimiter = "="
    userFile.readText().lines()
    return userFile.readText().lines().filter { it.contains(delimiter) }.map {
        val (name, detail) = it.split(delimiter)
        val (role, password) = detail.split(",")
        User(name, role, password)
    }
}

