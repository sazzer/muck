package uk.co.grahamcox.muck.service.acceptance.healthchecks

import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import uk.co.grahamcox.muck.service.acceptance.requester.Requester

/**
 * Cucumber steps for the healthchecks
 */
class HealthcheckSteps(
        private val requester: Requester
) {
    @When("^I get the health of the system$")
    fun checkHealth() {
        requester.get("/actuator/health")
    }

    @Then("^the system is healthy$")
    fun healthcheckResponseHealthy() {
        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, requester.lastResponse.statusCode) },
                Executable { Assertions.assertEquals("UP", requester.lastResponse.context.getValue("/body/status")) }
        )
    }
}
