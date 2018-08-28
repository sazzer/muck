package uk.co.grahamcox.muck.service.authentication.external.google

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

/**
 * Unit tests for [GoogleAuthenticationRedirectBuilderImpl]
 */
internal class GoogleAuthenticationRedirectBuilderImplTest {
    /**
     * Test building the redirect URI
     */
    @Test
    fun testBuildUri() {
        val testSubject  = GoogleAuthenticationRedirectBuilderImpl(
                authUri = URI("https://accounts.google.com/o/oauth2/v2/auth"),
                clientId = "abc123",
                redirectUri = URI("https://localhost:8080/authentication/google/redirect")
        )

        val result = testSubject.build()

        val uriComponents = UriComponentsBuilder.fromUri(result).build()
        val queryParams = uriComponents.queryParams

        Assertions.assertAll(
                Executable { Assertions.assertEquals("https", uriComponents.scheme) },
                Executable { Assertions.assertEquals("accounts.google.com", uriComponents.host) },
                Executable { Assertions.assertEquals("/o/oauth2/v2/auth", uriComponents.path) },

                Executable { Assertions.assertEquals(5, queryParams.size) },
                Executable { Assertions.assertEquals(listOf("abc123"), queryParams["client_id"]) },
                Executable { Assertions.assertEquals(listOf("code"), queryParams["response_type"]) },
                Executable { Assertions.assertEquals(listOf("openid%20email%20profile"), queryParams["scope"]) },
                Executable { Assertions.assertEquals(listOf("https://localhost:8080/authentication/google/redirect"), queryParams["redirect_uri"]) },
                Executable { Assertions.assertNotNull(queryParams["state"]) }
        )
    }
}
