package uk.co.grahamcox.muck.service.acceptance.authentication

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.http.client.MockClientHttpResponse
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.util.CollectionUtils
import org.springframework.web.util.UriComponentsBuilder
import uk.co.grahamcox.muck.service.acceptance.AcceptanceTestBase
import uk.co.grahamcox.muck.service.user.UserLogin
import uk.co.grahamcox.muck.service.user.UserService
import java.util.*

/**
 * Integration tests for completing authentication
 */
@Disabled("Need to support reading non-JSON responses")
class CompleteAuthenticationIT : AcceptanceTestBase() {
    /** The user service */
    @Autowired
    private lateinit var userService: UserService

    /**
     * Test completing authentication to Google for a new user
     */
    @Test
    fun testCompleteGoogleAuthNewUser() {
        val idToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IlRlc3QgVXNlciIsImdpdmVuX25hbWUiOiJUZXN0IiwiZmFtaWx5X25hbWUiOiJVc2VyIiwibG9jYWxlIjoiZW4tR0IiLCJpYXQiOjE1MzYwNzI4MzgsIm5iZiI6MTUzNjA3MjgzOCwiZXhwIjoxNTM2MDc2NDM4LCJhdWQiOiJ0ZXN0YXVkaWVuY2UtMTIzNDU2Nzg5MCIsImlzcyI6Imh0dHBzOi8vYWNjb3VudHMuZ29vZ2xlLmNvbSIsInN1YiI6InRlc3R1c2VyaWQtMTIzNDU2Nzg5MCJ9.4YgnNyu0KQhRXSUv9hDJhThcHfkD7sRTvN7d2p1XWy4"
        val rawAccessToken = """{"access_token": "googleAccessToken", "token_type": "Bearer", "expires_in": 3600, "id_token": "$idToken"}"""

        mockServer.expect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.requestTo("https://www.googleapis.com/oauth2/v4/token"))
                .andExpect(MockRestRequestMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockRestRequestMatchers.content().formData(
                        CollectionUtils.toMultiValueMap(mapOf(
                                "code" to "googleAuthCode",
                                "grant_type" to "authorization_code",
                                "client_id" to "googleClientId",
                                "client_secret" to "googleClientSecret",
                                "redirect_uri" to "http://example.com/google/callback"
                        ).mapValues { listOf(it.value) })
                ))
                .andRespond {
                    val response = MockClientHttpResponse(rawAccessToken.toByteArray(), HttpStatus.OK)
                    response.headers.contentType = MediaType.APPLICATION_JSON_UTF8
                    response
                }

        val googleId = "testuserid-1234567890"

        val uri = UriComponentsBuilder.fromPath("/api/authentication/external/google/callback")
                .queryParam("state", UUID.randomUUID().toString())
                .queryParam("code", "googleAuthCode")
                .queryParam("authuser", "0")
                .queryParam("session_state", UUID.randomUUID().toString())
                .queryParam("prompt", "consent")
                .build()
                .toUriString()

        val response = requester.get(uri)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },

                Executable {
                    val user = userService.getByProvider("google", googleId)

                    Assertions.assertNotNull(user)

                    user!!
                    Assertions.assertAll(
                            Executable { Assertions.assertEquals("test@example.com", user.data.email) },
                            Executable { Assertions.assertEquals("Test User", user.data.displayName) },
                            Executable { Assertions.assertEquals(1, user.data.logins.size) },

                            Executable { Assertions.assertTrue(user.data.logins.contains(UserLogin("google", googleId, "test@example.com"))) }
                        )
                }
        )
    }
}
