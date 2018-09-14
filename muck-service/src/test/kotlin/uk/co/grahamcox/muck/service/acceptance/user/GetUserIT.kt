package uk.co.grahamcox.muck.service.acceptance.user

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import uk.co.grahamcox.muck.service.acceptance.AcceptanceTestBase
import uk.co.grahamcox.muck.service.acceptance.seeder.DatabaseSeeder
import uk.co.grahamcox.muck.service.user.UserId
import java.util.*

/**
 * Acceptance tests for getting user details
 */
class GetUserIT : AcceptanceTestBase() {
    companion object {
        /** The ID of the user to get */
        val USER_ID = UUID.randomUUID()!!
    }

    /** The means to seed user records */
    @Autowired
    private lateinit var userSeeder: DatabaseSeeder

    /** The means to seed user provider links */
    @Autowired
    private lateinit var userProviderSeeder: DatabaseSeeder

    /**
     * Test getting the details of a user when no access token is provided
     */
    @Test
    fun getUserWhenNotAuthenticated() {
        val response = requester.get("/api/users/$USER_ID")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode) },
                Executable { Assertions.assertEquals(MediaType.valueOf("application/problem+json"), response.headers.contentType) },

                Executable { Assertions.assertEquals("tag:grahamcox.co.uk,2018,problems/missing-access-token", response.getValue("/body/type")) },
                Executable { Assertions.assertEquals("No Access Token was provided", response.getValue("/body/title")) },
                Executable { Assertions.assertEquals(401, response.getValue("/body/status")) }
        )
    }

    /**
     * Test getting the details of a user that doesn't exist when authenticated as that user
     */
    @Test
    fun getUnknownUser() {
        authenticatedAs(UserId(USER_ID))
        val response = requester.get("/api/users/$USER_ID")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode) },
                Executable { Assertions.assertEquals(MediaType.valueOf("application/problem+json"), response.headers.contentType) },

                Executable { Assertions.assertEquals("tag:grahamcox.co.uk,2018,problems/users/unknown-user", response.getValue("/body/type")) },
                Executable { Assertions.assertEquals("The requested user was not found", response.getValue("/body/title")) },
                Executable { Assertions.assertEquals(404, response.getValue("/body/status")) }
        )
    }

    /**
     * Test getting the details of a user when authenticated as a different user
     */
    @Test
    fun getOtherUser() {
        authenticatedAs(UserId(USER_ID))
        val response = requester.get("/api/users/${UUID.randomUUID()}")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.FORBIDDEN, response.statusCode) },
                Executable { Assertions.assertEquals(MediaType.valueOf("application/problem+json"), response.headers.contentType) },

                Executable { Assertions.assertEquals("tag:grahamcox.co.uk,2018,problems/access-denied", response.getValue("/body/type")) },
                Executable { Assertions.assertEquals("Access denied to this resource", response.getValue("/body/title")) },
                Executable { Assertions.assertEquals(403, response.getValue("/body/status")) }
        )
    }

    /**
     * Test getting the details of a user when authenticated as that user
     */
    @Test
    fun getCurrentUser() {
        userSeeder.seed(mapOf(
                "id" to USER_ID.toString(),
                "email" to "test@example.com",
                "displayName" to "Test User"
        ))

        userProviderSeeder.seed(mapOf(
                "userId" to USER_ID.toString(),
                "provider" to "google",
                "providerId" to "1234321",
                "displayName" to "Test User Account"
        ))

        authenticatedAs(UserId(USER_ID))
        val response = requester.get("/api/users/$USER_ID")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.valueOf("application/hal+json"))) },

                Executable { Assertions.assertEquals("http://localhost/api/users/$USER_ID",
                        convertReturnedUri(response.getValue("/body/_links/self/href"))) },
                Executable { Assertions.assertEquals("application/hal+json", response.getValue("/body/_links/self/type")) },
                Executable { Assertions.assertEquals(false, response.getValue("/body/_links/self/templated")) },

                Executable { Assertions.assertEquals("test@example.com", response.getValue("/body/email")) },
                Executable { Assertions.assertEquals("Test User", response.getValue("/body/displayName")) },

                Executable { Assertions.assertEquals(1.0, response.getValue("count(/body/logins)")) },
                Executable { Assertions.assertEquals("google", response.getValue("/body/logins[1]/provider")) },
                Executable { Assertions.assertEquals("1234321", response.getValue("/body/logins[1]/providerId")) },
                Executable { Assertions.assertEquals("Test User Account", response.getValue("/body/logins[1]/displayName")) }
        )
    }
}
