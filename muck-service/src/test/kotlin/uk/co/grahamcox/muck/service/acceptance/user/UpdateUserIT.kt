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
                convertFromJson("""{
                    "email": "new@user.com",
                    "displayName": "New User",
                    "logins": [
                        {
                            "provider": "facebook",
                            "providerId": "1232123",
                            "displayName": "New User"
                        }
                    ]
                }"""))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.valueOf("application/hal+json"))) },

                Executable { Assertions.assertEquals(convertFromJson("""{
                    "_links": {
                        "self": {
                            "href": "${buildUri("/api/users/$USER_ID")}",
                            "templated": false,
                            "type": "application/hal+json"
                        }
                    },
                    "email": "new@user.com",
                    "displayName": "New User",
                    "logins": [
                        {
                            "provider": "facebook",
                            "providerId": "1232123",
                            "displayName": "New User"
                        }
                    ]
                }"""), response.getValue("/body")) }
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
                convertFromJson("""{
                    "email": "new@user.com",
                    "displayName": "New User",
                    "logins": [
                        {
                            "provider": "facebook",
                            "providerId": "1232123",
                            "displayName": "New User"
                        }
                    ]
                }"""))

        val response = requester.get("/api/users/$USER_ID")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.valueOf("application/hal+json"))) },

                Executable { Assertions.assertEquals(convertFromJson("""{
                    "_links": {
                        "self": {
                            "href": "${buildUri("/api/users/$USER_ID")}",
                            "templated": false,
                            "type": "application/hal+json"
                        }
                    },
                    "email": "new@user.com",
                    "displayName": "New User",
                    "logins": [
                        {
                            "provider": "facebook",
                            "providerId": "1232123",
                            "displayName": "New User"
                        }
                    ]
                }"""), response.getValue("/body")) }
        )
    }
}
