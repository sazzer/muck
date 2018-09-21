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
 * Acceptance tests for updating user details
 */
class UpdateUserIT : AcceptanceTestBase() {
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
     * Test updating a user profile
     */
    @Test
    fun updateUserProfileData() {
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

        val response = requester.put("/api/users/$USER_ID",
                mapOf(
                        "email" to "new@user.com",
                        "displayName" to "New User",
                        "logins" to listOf(
                                mapOf(
                                        "provider" to "facebook",
                                        "providerId" to "1232123",
                                        "displayName" to "New User"
                                )
                        )
                ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.valueOf("application/hal+json"))) },

                Executable { Assertions.assertEquals("http://localhost/api/users/$USER_ID",
                        convertReturnedUri(response.getValue("/body/_links/self/href"))) },
                Executable { Assertions.assertEquals("application/hal+json", response.getValue("/body/_links/self/type")) },
                Executable { Assertions.assertEquals(false, response.getValue("/body/_links/self/templated")) },

                Executable { Assertions.assertEquals("new@user.com", response.getValue("/body/email")) },
                Executable { Assertions.assertEquals("New User", response.getValue("/body/displayName")) },

                Executable { Assertions.assertEquals(1.0, response.getValue("count(/body/logins)")) },
                Executable { Assertions.assertEquals("facebook", response.getValue("/body/logins[1]/provider")) },
                Executable { Assertions.assertEquals("1232123", response.getValue("/body/logins[1]/providerId")) },
                Executable { Assertions.assertEquals("New User", response.getValue("/body/logins[1]/displayName")) }
        )
    }

    /**
     * Test updating a user profile
     */
    @Test
    fun updateAndReloadUserProfileData() {
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

        requester.put("/api/users/$USER_ID",
                mapOf(
                        "email" to "new@user.com",
                        "displayName" to "New User",
                        "logins" to listOf(
                                mapOf(
                                        "provider" to "facebook",
                                        "providerId" to "1232123",
                                        "displayName" to "New User"
                                )
                        )
                ))

        val response = requester.get("/api/users/$USER_ID")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.valueOf("application/hal+json"))) },

                Executable { Assertions.assertEquals("http://localhost/api/users/$USER_ID",
                        convertReturnedUri(response.getValue("/body/_links/self/href"))) },
                Executable { Assertions.assertEquals("application/hal+json", response.getValue("/body/_links/self/type")) },
                Executable { Assertions.assertEquals(false, response.getValue("/body/_links/self/templated")) },

                Executable { Assertions.assertEquals("new@user.com", response.getValue("/body/email")) },
                Executable { Assertions.assertEquals("New User", response.getValue("/body/displayName")) },

                Executable { Assertions.assertEquals(1.0, response.getValue("count(/body/logins)")) },
                Executable { Assertions.assertEquals("facebook", response.getValue("/body/logins[1]/provider")) },
                Executable { Assertions.assertEquals("1232123", response.getValue("/body/logins[1]/providerId")) },
                Executable { Assertions.assertEquals("New User", response.getValue("/body/logins[1]/displayName")) }
        )
    }
}
