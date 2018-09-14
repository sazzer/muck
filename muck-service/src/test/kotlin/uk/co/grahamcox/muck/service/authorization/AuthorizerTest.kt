package uk.co.grahamcox.muck.service.authorization

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import uk.co.grahamcox.muck.service.authentication.AccessToken
import uk.co.grahamcox.muck.service.authentication.AccessTokenId
import uk.co.grahamcox.muck.service.user.UserId
import java.time.Instant
import java.util.*

/**
 * Unit tests for [AuthorizationContext]
 */
internal class AuthorizerTest {
    /** The user to authorize */
    private val user = UserId(UUID.fromString("00000000-0000-0000-0000-000000000000"))

    /** The access token to authorize */
    private val accessToken = AccessToken(
            id = AccessTokenId("accessTokenId"),
            user = user,
            created = Instant.parse("2018-06-03T12:09:00Z"),
            expires = Instant.parse("2019-06-03T12:09:00Z")
    )

    /** The test subject */
    private val testSubject = Authorizer(accessToken)

    /**
     * Test when the current user is the expected one
     */
    @Test
    fun testIsCurrentUser() {
        testSubject {
            isUser(user)
        }
    }

    /**
     * Test when the current user is not the expected one
     */
    @Test
    fun testIsNotCurrentUser() {
        Assertions.assertThrows(AuthorizationFailedException::class.java) {
            testSubject {
                isUser(UserId(UUID.randomUUID()))
            }
        }
    }
}
