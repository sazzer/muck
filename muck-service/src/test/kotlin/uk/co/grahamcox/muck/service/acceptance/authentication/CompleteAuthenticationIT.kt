package uk.co.grahamcox.muck.service.acceptance.authentication

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.util.UriComponentsBuilder
import uk.co.grahamcox.muck.service.acceptance.AcceptanceTestBase
import uk.co.grahamcox.muck.service.user.UserLogin
import uk.co.grahamcox.muck.service.user.UserService
import java.util.*

/**
 * Integration tests for completing authentication
 */
@Disabled
class CompleteAuthenticationIT : AcceptanceTestBase() {
    /** The user service */
    @Autowired
    private lateinit var userService: UserService

    /**
     * Test completing authentication to Google for a new user
     */
    @Test
    fun testCompleteGoogleAuthNewUser() {
        val googleId = UUID.randomUUID().toString()

        val uri = UriComponentsBuilder.fromPath("/api/authentication/external/google/callback")
                .queryParam("state", UUID.randomUUID().toString())
                .queryParam("code", UUID.randomUUID().toString())
                .queryParam("authuser", "0")
                .queryParam("session_state", UUID.randomUUID().toString())
                .queryParam("prompt", "consent")
                .build()
                .toUriString()

        val response = requester.get(uri)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },

                Executable {
                    val user = userService.getByProvider("google", googleId)

                    Assertions.assertNotNull(user)

                    user!!
                    Assertions.assertAll(
                            Executable { Assertions.assertEquals("graham@example.com", user.data.email) },
                            Executable { Assertions.assertEquals("Graham", user.data.displayName) },
                            Executable { Assertions.assertEquals(1, user.data.logins.size) },

                            Executable { Assertions.assertTrue(user.data.logins.contains(UserLogin("google", googleId, "graham@example.com"))) }
                        )
                }
        )
    }
}
