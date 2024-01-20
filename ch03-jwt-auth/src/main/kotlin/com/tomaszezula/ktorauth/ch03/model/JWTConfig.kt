package com.tomaszezula.ktorauth.ch03.model

data class JWTConfig(
    val realm: String,
    val secret: String,
    val audience: String,
    val issuer: String,
    val expirationSeconds: Long
)