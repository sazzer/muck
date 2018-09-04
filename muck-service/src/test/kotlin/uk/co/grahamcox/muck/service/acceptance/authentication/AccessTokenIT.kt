package uk.co.grahamcox.muck.service.acceptance.authentication

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import uk.co.grahamcox.muck.service.acceptance.AcceptanceTestBase
import uk.co.grahamcox.muck.service.user.UserId
import java.util.*

/**
 * Tests for when an access token is or isn't present
 */
class AccessTokenIT : AcceptanceTestBase() {
    /**
     * Test when an access token is required but not present
     */
    @Test
    fun testAccessTokenMissingRequired() {
        val response = requester.get("/debug/authentication/token/required")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode) },
                Executable { Assertions.assertEquals(MediaType.valueOf("application/problem+json"), response.headers.contentType) },

                Executable { Assertions.assertEquals("tag:grahamcox.co.uk,2018,problems/missing-access-token", response.getValue("/body/type")) },
                Executable { Assertions.assertEquals("No Access Token was provided", response.getValue("/body/title")) },
                Executable { Assertions.assertEquals(401, response.getValue("/body/status")) }
        )
    }

    /**
     * Test when a user ID is required but not present
     */
    @Test
    fun testUserIdMissingRequired() {
        val response = requester.get("/debug/authentication/user/required")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode) },
                Executable { Assertions.assertEquals(MediaType.valueOf("application/problem+json"), response.headers.contentType) },

                Executable { Assertions.assertEquals("tag:grahamcox.co.uk,2018,problems/missing-access-token", response.getValue("/body/type")) },
                Executable { Assertions.assertEquals("No Access Token was provided", response.getValue("/body/title")) },
                Executable { Assertions.assertEquals(401, response.getValue("/body/status")) }
        )
    }

    /**
     * Test when an access token is required but not invalid
     */
    @Test
    fun testAccessTokenInvalidRequired() {
        requester.accessToken = "invalid"
        val response = requester.get("/debug/authentication/token/required")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.FORBIDDEN, response.statusCode) },
                Executable { Assertions.assertEquals(MediaType.valueOf("application/problem+json"), response.headers.contentType) },

                Executable { Assertions.assertEquals("tag:grahamcox.co.uk,2018,problems/invalid-access-token", response.getValue("/body/type")) },
                Executable { Assertions.assertEquals("The Access Token was invalid", response.getValue("/body/title")) },
                Executable { Assertions.assertEquals(403, response.getValue("/body/status")) }
        )
    }


    /**
     * Test when an access token is present and correct
     */
    @Test
    fun testAccessTokenValid() {
        val userId = UUID.randomUUID()
        authenticatedAs(UserId(userId))
        val response = requester.get("/debug/authentication/token/required")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },

                Executable { Assertions.assertEquals(userId.toString(), response.getValue("/body/user/id")) }
        )
    }

}
