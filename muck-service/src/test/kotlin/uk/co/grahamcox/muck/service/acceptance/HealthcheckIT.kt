package uk.co.grahamcox.muck.service.acceptance

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import uk.co.grahamcox.muck.service.acceptance.requester.Requester
import uk.co.grahamcox.muck.service.spring.SpringTestBase

/**
 * Integration Test for the Healthchecks
 */
class HealthcheckIT : SpringTestBase() {
    /** Means to make API calls */
    @Autowired
    private lateinit var requester: Requester

    /**
     * Test the healthchecks return that the system is healthy
     */
    @Test
    fun testHealthchecks() {
        val response = requester.get("/actuator/health")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },
                Executable { Assertions.assertEquals("UP", response.context.getValue("/body/status")) }
        )
    }
}
