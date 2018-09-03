package uk.co.grahamcox.muck.service.acceptance.authentication

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.util.UriComponentsBuilder
import uk.co.grahamcox.muck.service.acceptance.AcceptanceTestBase

/**
 * Test starting to authenticate
 */
class StartAuthenticationIT : AcceptanceTestBase() {

    /**
     * Test starting to authenticate against an unknown authentication service
     */
    @Test
    fun startUnknownAuth() {
        val response = requester.get("/api/authentication/external/unknown/start")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode) },
                Executable { Assertions.assertEquals(MediaType.valueOf("application/problem+json"), response.headers.contentType) },

                Executable { Assertions.assertEquals("tag:grahamcox.co.uk,2018,problems/unknown-authentication-service", response.getValue("/body/type")) },
                Executable { Assertions.assertEquals("The requested Authentication Service was unknown", response.getValue("/body/title")) },
                Executable { Assertions.assertEquals(404, response.getValue("/body/status")) },
                Executable { Assertions.assertEquals("unknown", response.getValue("/body/authenticationService")) }
        )
    }

    /**
     * Test starting to authenticate against Google
     */
    @Test
    fun startGoogleAuth() {
        val response = requester.get("/api/authentication/external/google/start")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.FOUND, response.statusCode) },
                Executable {
                    Executable { Assertions.assertNotNull(response.headers.location) }
                    val uriComponents = UriComponentsBuilder.fromUri(response.headers.location!!).build()

                    Assertions.assertAll(
                            Executable { Assertions.assertEquals("https", uriComponents.scheme) },
                            Executable { Assertions.assertEquals("accounts.google.com", uriComponents.host) },
                            Executable { Assertions.assertEquals("/o/oauth2/v2/auth", uriComponents.path) },
                            Executable { Assertions.assertEquals(-1, uriComponents.port) },

                            Executable { Assertions.assertEquals(5, uriComponents.queryParams.size) },
                            Executable { Assertions.assertEquals(listOf("googleClientId"), uriComponents.queryParams["client_id"]) },
                            Executable { Assertions.assertEquals(listOf("code"), uriComponents.queryParams["response_type"]) },
                            Executable { Assertions.assertEquals(listOf("openid%20email%20profile"), uriComponents.queryParams["scope"]) },
                            Executable { Assertions.assertEquals(listOf("http://example.com/google/callback"), uriComponents.queryParams["redirect_uri"]) },
                            Executable { Assertions.assertTrue(uriComponents.queryParams["state"]!!.isNotEmpty()) }
                    )
                }
        )
    }
}
