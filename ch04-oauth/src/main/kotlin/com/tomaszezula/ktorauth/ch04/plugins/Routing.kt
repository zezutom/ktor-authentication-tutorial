package com.tomaszezula.ktorauth.ch04.plugins

import com.tomaszezula.ktorauth.ch04.model.UserInfo
import com.tomaszezula.ktorauth.ch04.model.UserSession
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.configureRouting(oauthName: String, redirects: MutableMap<String, String>) {
    routing {
        get("/home") {
            val session = call.sessions.get<UserSession>()
            if (session == null) {
                call.respondText("Not logged in")
            } else {
                call.respondText("Hello, ${session.state}!")
            }
        }
        authenticate(oauthName) {
            get("/login") {
                // Automatically redirects to `authorizeUrl`
            }
            get("/callback") {
                // Receives the authorization code and exchanges it for an access token
                (call.principal() as OAuthAccessTokenResponse.OAuth2?)?.let { principal ->
                    principal.state?.let { state ->
                        call.sessions.set(UserSession(state, principal.accessToken))
                        redirects[state]?.let { redirectUrl ->
                            call.respondRedirect(redirectUrl)
                            return@get
                        }
                    }
                    call.respondRedirect("/home")
                }
            }
            get("/me") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respondText("Not logged in")
                } else {
                    val httpClient = HttpClient()
                    val userInfo = getUserInfo(session, httpClient)
                    call.respondText("Hi, ${userInfo.name}!")
                }
            }
        }
    }
}

private suspend fun getUserInfo(userSession: UserSession, httpClient: HttpClient): UserInfo {
    return httpClient.get("https://www.googleapis.com/oauth2/v1/userinfo") {
        headers {
            append("Authorization", "Bearer ${userSession.token}")
        }
    }.body()
}