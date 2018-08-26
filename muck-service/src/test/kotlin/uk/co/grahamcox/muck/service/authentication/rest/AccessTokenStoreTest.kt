package uk.co.grahamcox.muck.service.authentication.rest

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.co.grahamcox.muck.service.CurrentRequest
import uk.co.grahamcox.muck.service.authentication.AccessToken
import uk.co.grahamcox.muck.service.authentication.AccessTokenId
import uk.co.grahamcox.muck.service.user.UserId
import java.time.Instant
import java.util.*

/**
 * Unit tests for the Access Token Store
 */
@CurrentRequest
internal class AccessTokenStoreTest {
    /** The test subject */
    private val testSubject = AccessTokenStore()

    /**
     * Test getting the access token when it was never set
     */
    @Test
    fun testGetWhenNotSet() {
        Assertions.assertNull(testSubject.accessToken)
    }

    /**
     * Test setting the access token and then getting it back again
     */
    @Test
    fun testGetWhenSet() {
        val accessToken = AccessToken(
                id = AccessTokenId("accessToken"),
                user = UserId(UUID.randomUUID()),
                created = Instant.EPOCH,
                expires = Instant.MAX
        )

        testSubject.accessToken = accessToken
        Assertions.assertSame(accessToken, testSubject.accessToken)
    }

    /**
     * Test getting the access token after it's been reset
     */
    @Test
    fun testGetWhenReset() {
        val accessToken = AccessToken(
                id = AccessTokenId("accessToken"),
                user = UserId(UUID.randomUUID()),
                created = Instant.EPOCH,
                expires = Instant.MAX
        )

        testSubject.accessToken = accessToken
        testSubject.reset()
        Assertions.assertNull(testSubject.accessToken)
    }
}
