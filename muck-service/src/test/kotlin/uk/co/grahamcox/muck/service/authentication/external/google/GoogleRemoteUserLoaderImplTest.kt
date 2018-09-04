package uk.co.grahamcox.muck.service.authentication.external.google

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

/**
 * Unit tests for the [GoogleRemoteUserLoaderImpl]
 */
internal class GoogleRemoteUserLoaderImplTest {
    /** The Access Token Loader */
    private val googleAccessTokenLoader: GoogleAccessTokenLoader = mockk()

    /** The test subject */
    private val testSubject = GoogleRemoteUserLoaderImpl(googleAccessTokenLoader)

    /**
     * Test loading the remote user details from a provided ID Token
     */
    @Test
    fun testLoadUserFromIdToken() {
        every { googleAccessTokenLoader.retrieveAccessToken("authCode") } returns GoogleAccessToken(
                accessToken = "accessToken",
                expiresIn = 3600,
                tokenType = "Bearer",
                idToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IlRlc3QgVXNlciIsImdpdmVuX25hbWUiOiJUZXN0IiwiZmFtaWx5X25hbWUiOiJVc2VyIiwibG9jYWxlIjoiZW4tR0IiLCJpYXQiOjE1MzYwNzI4MzgsIm5iZiI6MTUzNjA3MjgzOCwiZXhwIjoxNTM2MDc2NDM4LCJhdWQiOiJ0ZXN0YXVkaWVuY2UtMTIzNDU2Nzg5MCIsImlzcyI6Imh0dHBzOi8vYWNjb3VudHMuZ29vZ2xlLmNvbSIsInN1YiI6InRlc3R1c2VyaWQtMTIzNDU2Nzg5MCJ9.4YgnNyu0KQhRXSUv9hDJhThcHfkD7sRTvN7d2p1XWy4"
        )
        val remoteUser = testSubject.loadRemoteUser(mapOf("code" to "authCode"))

        Assertions.assertAll(
                Executable { Assertions.assertEquals("testuserid-1234567890", remoteUser.providerId) },
                Executable { Assertions.assertEquals("test@example.com", remoteUser.providerDisplayName) },
                Executable { Assertions.assertEquals("test@example.com", remoteUser.email) },
                Executable { Assertions.assertEquals("Test User", remoteUser.displayName) }
        )
    }
}
