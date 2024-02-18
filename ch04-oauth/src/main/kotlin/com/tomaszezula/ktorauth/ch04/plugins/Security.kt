package com.tomaszezula.ktorauth.ch04.plugins

import com.tomaszezula.ktorauth.ch04.model.OAuthConfig
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureSecurity(
    oauthName: String, oauthConfig: OAuthConfig,
    httpClient: HttpClient,
    redirects: MutableMap<String, String>
) {
    authentication {
        oauth(oauthName) {
            urlProvider = { "http://localhost:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = oauthConfig.name,
                    authorizeUrl = oauthConfig.authorizeUrl,
                    accessTokenUrl = oauthConfig.accessTokenUrl,
                    requestMethod = HttpMethod.Post,
                    clientId = oauthConfig.clientId,
                    clientSecret = oauthConfig.clientSecret,
                    defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile"),
                    extraAuthParameters = listOf("access_type" to "offline"),
                    onStateCreated = { call, state ->
                        //saves new state with redirect url value
                        call.request.queryParameters["redirectUrl"]?.let {
                            redirects[state] = it
                        }
                    }
                )
            }
            client = httpClient
        }
    }
}
