package uk.co.grahamcox.muck.service.acceptance.authentication

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import uk.co.grahamcox.muck.service.acceptance.AcceptanceTestBase

/**
 * Integration tests for listing the available authentication services
 */
class ListServicesIT : AcceptanceTestBase() {
    /**
     * List the services and ensure that we get the correct ones back
     */
    @Test
    fun listServices() {
        val response = requester.get("/api/authentication/external")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertTrue(response.headers.contentType!!.isCompatibleWith(MediaType.APPLICATION_JSON)) },

                Executable { Assertions.assertEquals(convertFromJson("""{
                    "services": [
                        "google"
                    ]
                }"""), response.getValue("/body")) }
        )
    }
}
