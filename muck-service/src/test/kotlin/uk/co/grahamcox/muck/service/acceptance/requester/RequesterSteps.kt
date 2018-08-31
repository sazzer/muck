package uk.co.grahamcox.muck.service.acceptance.requester

import cucumber.api.java.Before
import cucumber.api.java.en.Then
import org.junit.jupiter.api.Assertions
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

/**
 * Cucumber steps for working directly with the requester
 */
class RequesterSteps(
        private val requester: Requester
) {
    @Before
    fun setup() {
        requester.reset()
    }

    @Then("^I get an? (.+) response$")
    fun checkResponseCode(statusName: String) {
        val convertedName = statusName.toUpperCase().replace(" ", "_")
        val httpStatus = HttpStatus.values().find { it.name == convertedName }

        Assertions.assertNotNull(httpStatus, "Unknown Status: $statusName")
        Assertions.assertEquals(httpStatus, requester.lastResponse.statusCode)
    }

    @Then("^I get an? (.+) document$")
    fun checkContentType(contentType: String) {
        val mediaType = MediaType.parseMediaType(contentType)
        Assertions.assertTrue(requester.lastResponse.response.headers.contentType!!.isCompatibleWith(mediaType))
    }
}
