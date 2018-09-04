package uk.co.grahamcox.muck.service.authentication.external.google

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.http.client.MockClientHttpResponse
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.util.CollectionUtils
import org.springframework.web.client.RestTemplate
import java.net.URI

/**
 * Unit tests for the Google Access Token Loader
 */
internal class GoogleAccessTokenLoaderTest {
    /** The rest template tester to use */
    private val restTemplate = RestTemplate()

    /** The mock server the Rest Template calls */
    private val mockServer = MockRestServiceServer.bindTo(restTemplate).build()

    /** The test subject */
    private val testSubject = GoogleAccessTokenLoader(
            restTemplate,
            URI("https://www.googleapis.com/oauth2/v4/token"),
            "googleClientId",
            "googleClientSecret",
            URI("http://localhost:8080/google/callback")
    )

    /**
     * Test successfully getting an access token
     */
    @Test
    fun testGetAccessTokenSuccessfully() {
        val rawAccessToken = """{"access_token": "googleAccessToken", "token_type": "Bearer", "expires_in": 3600, "id_token": "googleIdToken"}"""

        mockServer.expect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.requestTo("https://www.googleapis.com/oauth2/v4/token"))
                .andExpect(MockRestRequestMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockRestRequestMatchers.content().formData(
                        CollectionUtils.toMultiValueMap(mapOf(
                                "code" to "googleAuthCode",
                                "grant_type" to "authorization_code",
                                "client_id" to "googleClientId",
                                "client_secret" to "googleClientSecret",
                                "redirect_uri" to "http://localhost:8080/google/callback"
                        ).mapValues { listOf(it.value) })
                ))
                .andRespond {
                    val response = MockClientHttpResponse(rawAccessToken.toByteArray(), HttpStatus.OK)
                    response.headers.contentType = MediaType.APPLICATION_JSON_UTF8
                    response
                }
        val accessToken = testSubject.retrieveAccessToken("googleAuthCode")

        Assertions.assertAll(
                Executable { Assertions.assertEquals("googleAccessToken", accessToken.accessToken) },
                Executable { Assertions.assertEquals(3600, accessToken.expiresIn) },
                Executable { Assertions.assertEquals("Bearer", accessToken.tokenType) },
                Executable { Assertions.assertEquals("googleIdToken", accessToken.idToken) }
        )
    }
}
