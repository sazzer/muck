package uk.co.grahamcox.muck.service.acceptance.user

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import uk.co.grahamcox.muck.service.acceptance.AcceptanceTestBase
import uk.co.grahamcox.muck.service.acceptance.requester.Response
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
     * Seed the user to work with
     */
    @BeforeEach
    private fun seedUser() {
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
    }

    /**
     * Test updating a user profile when not logged in
     */
    @Test
    fun updateUserWhenNotAuthenticated() {
        clearRequester()

        val response = performUpdateRequest()

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode) },
                Executable { Assertions.assertEquals(MediaType.valueOf("application/problem+json"), response.headers.contentType) },

                Executable { Assertions.assertEquals(convertFromJson("""{
                    "type": "tag:grahamcox.co.uk,2018,problems/missing-access-token",
                    "title": "No Access Token was provided",
                    "status": 401
                }"""), response.getValue("/body")) }
        )
    }

    /**
     * Test updating a user profile when logged in as a different user
     */
    @Test
    fun updateAnotherUser() {
        authenticatedAs(UserId(UUID.randomUUID()))

        val response = performUpdateRequest()

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.FORBIDDEN, response.statusCode) },
                Executable { Assertions.assertEquals(MediaType.valueOf("application/problem+json"), response.headers.contentType) },

                Executable { Assertions.assertEquals(convertFromJson("""{
                    "type": "tag:grahamcox.co.uk,2018,problems/access-denied",
                    "title": "Access denied to this resource",
                    "status": 403
                }"""), response.getValue("/body")) }
        )
    }

    /**
     * Test updating a user profile
     */
    @Test
    fun updateUserProfileData() {
        val response = performUpdateRequest()

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
                    "id": "$USER_ID",
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
        performUpdateRequest()

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
                    "id": "$USER_ID",
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
     * Test updating a user profile that doesn't exist
     */
    @Test
    fun updateUnknownUser() {
        val userId = UserId(UUID.randomUUID())
        authenticatedAs(userId)

        val response = requester.put("/api/users/${userId.id}",
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
                Executable { Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode) },
                Executable { Assertions.assertEquals(MediaType.valueOf("application/problem+json"), response.headers.contentType) },

                Executable { Assertions.assertEquals(convertFromJson("""{
                    "type": "tag:grahamcox.co.uk,2018,problems/users/unknown-user",
                    "title": "The requested user was not found",
                    "status": 404
                }"""), response.getValue("/body")) }
        )
    }

    /**
     * Test updating a user profile with invalid data
     */
    @Test
    fun updateUserProfileInvalidData() {
        val response = requester.put("/api/users/$USER_ID",
                convertFromJson("""{
                        "email": "",
                        "displayName": "",
                        "logins": [
                            {
                                "provider": "",
                                "providerId": "",
                                "displayName": ""
                            }
                        ]
                    }"""))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },
                Executable { Assertions.assertEquals(MediaType.valueOf("application/problem+json"), response.headers.contentType) },

                Executable { Assertions.assertEquals(convertFromJson("""{
                    "type": "tag:grahamcox.co.uk,2018,problems/invalid-request",
                    "title": "The incoming request was invalid",
                    "status": 400,
                    "violations": [
                        {
                            "field": "displayName",
                            "title": "must not be blank",
                            "type": "javax.validation.constraints.NotBlank",
                            "value": ""
                        }, {
                            "field": "email",
                            "title": "must not be blank",
                            "type": "javax.validation.constraints.NotBlank",
                            "value": ""
                        }, {
                            "field": "logins[].displayName",
                            "title": "must not be blank",
                            "type": "javax.validation.constraints.NotBlank",
                            "value": ""
                        }, {
                            "field": "logins[].provider",
                            "title": "must not be blank",
                            "type": "javax.validation.constraints.NotBlank",
                            "value": ""
                        }, {
                            "field": "logins[].providerId",
                            "title": "must not be blank",
                            "type": "javax.validation.constraints.NotBlank",
                            "value": ""
                        }
                    ]
                }"""), response.getValue("/body")) }
        )
    }

    /**
     * Actually perform the request to update the user
     */
    private fun performUpdateRequest(): Response {
        return requester.put("/api/users/$USER_ID",
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
    }
}
