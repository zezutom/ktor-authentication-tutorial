package com.tomaszezula.ktorauth.ch04.plugins

import com.tomaszezula.ktorauth.ch04.model.JWTConfig
import com.tomaszezula.ktorauth.ch04.model.OAuthConfig
import com.tomaszezula.ktorauth.ch04.model.UserInfo
import com.tomaszezula.ktorauth.ch04.model.createToken
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Clock

fun Application.configureRouting(jwtConfig: JWTConfig, oauthConfig: OAuthConfig, httpClient: HttpClient, clock: Clock) {
    routing {
        get("/home") {
            val principal = call.principal<JWTPrincipal>() ?: run {
                call.respondText("Not logged in")
                return@get
            }
            call.respondText("Hello, ${principal}!")
        }
        authenticate(jwtConfig.name) {
            get("/me") {
                val principal = call.principal<JWTPrincipal>() ?: run {
                    call.respond(HttpStatusCode.Forbidden, "Not logged in")
                    return@get
                }
                val accessToken = principal.getClaim("google_access_token", String::class) ?: run {
                    call.respond(HttpStatusCode.Forbidden, "No access token")
                    return@get
                }
                val userInfo = getUserInfo(accessToken, oauthConfig, httpClient)
                call.respondText("Hi, ${userInfo.name}!")
            }
        }
        authenticate(oauthConfig.name) {
            get("/login") {
                // Automatically redirects to `authorizeUrl`
            }
            get("/callback") {
                // Receives the authorization code and exchanges it for an access token
                (call.principal() as OAuthAccessTokenResponse.OAuth2?)?.let { principal ->
                    val accessToken = principal.accessToken
                    val jwtToken = jwtConfig.createToken(clock, accessToken, 3600)
                    call.respondText(jwtToken, contentType = ContentType.Text.Plain)
                }
            }
        }
    }
}

private suspend fun getUserInfo(accessToken: String, oauthConfig: OAuthConfig, httpClient: HttpClient): UserInfo {
    return httpClient.get(oauthConfig.userInfoUrl) {
        headers {
            append("Authorization", "Bearer $accessToken")
        }
    }.body()
}