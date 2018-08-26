package uk.co.grahamcox.muck.service.authentication

import io.mockk.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.co.grahamcox.muck.service.user.UserId
import java.time.Instant
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Unit tests for the Access Token Interceptor
 */
internal class AccessTokenInterceptorTest {
    /** The access token store */
    private val accessTokenStore: AccessTokenStore = mockk()

    /** The access token serializer */
    private val accessTokenSerializer: AccessTokenSerializer = mockk()

    /** The incoming request */
    private val request: HttpServletRequest = mockk()

    /** The outgoing response */
    private val response: HttpServletResponse = mockk()

    /** The handler */
    private val handler = "Handler"

    /** the test subject */
    private val testSubject = AccessTokenInterceptor(accessTokenStore, accessTokenSerializer)

    /**
     * Test that we reset the store at the end of the request
     */
    @Test
    fun testEndRequest() {
        every { accessTokenStore.reset() } just Runs

        testSubject.afterCompletion(request, response, handler, null)

        verify { accessTokenStore.reset() }
    }

    /**
     * Test that when there was no Authorization header then nothing happens
     */
    @Test
    fun testNoAuthHeader() {
        every { request.getHeader("Authorization") } returns null

        testSubject.preHandle(request, response, handler)
    }

    /**
     * Test that when there was an Authorization header but it wasn't a Bearer token then nothing happens
     */
    @Test
    fun testNoBearerAuthHeader() {
        every { request.getHeader("Authorization") } returns "Basic abc123"

        testSubject.preHandle(request, response, handler)
    }

    /**
     * Test that when there was an Authorization header but it wasn't a valid Bearer token then an error occurs
     */
    @Test
    fun testNoValidBearerAuthHeader() {
        every { request.getHeader("Authorization") } returns "Bearer abc123"
        every { accessTokenSerializer.deserialize("abc123") } throws InvalidAccessTokenException("Oops")

        Assertions.assertThrows(InvalidAccessTokenException::class.java) {
            testSubject.preHandle(request, response, handler)
        }
    }

    /**
     * Test that when there was an Authorization header and it was a valid Bearer token then the access token is stored
     */
    @Test
    fun testValidBearerAuthHeader() {
        val accessToken = AccessToken(
                id = AccessTokenId("accessToken"),
                user = UserId(UUID.randomUUID()),
                created = Instant.EPOCH,
                expires = Instant.MAX
        )

        every { request.getHeader("Authorization") } returns "Bearer abc123"
        every { accessTokenSerializer.deserialize("abc123") } returns accessToken
        every { accessTokenStore.accessToken = accessToken } just Runs

        testSubject.preHandle(request, response, handler)

        verify { accessTokenStore.accessToken = accessToken }
    }
}
