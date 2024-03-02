package com.tomaszezula.ktorauth.ch05

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.HttpHeaders.AccessControlRequestMethod
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class CorsTest {

    @Test
    fun `test CORS options`() = testApplication {
        application {
            // Reuse the same CORS configuration as in the main Application module
        }
        client.options("/") {
            header(HttpHeaders.Origin, "https://www.example.com")
            header(AccessControlRequestMethod, "GET")
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                "https://www.example.com",
                headers[HttpHeaders.AccessControlAllowOrigin]
            )
            assertEquals(
                "true",
                headers[HttpHeaders.AccessControlAllowCredentials]
            )
        }
    }
}