package com.tomaszezula.ktorauth.ch04.model

data class OAuthConfig(
    val name: String,
    val clientId: String,
    val clientSecret: String,
    val accessTokenUrl: String,
    val authorizeUrl: String,
    val redirectUrl: String,
    val userInfoUrl: String,
    val defaultScopes: List<String>
)