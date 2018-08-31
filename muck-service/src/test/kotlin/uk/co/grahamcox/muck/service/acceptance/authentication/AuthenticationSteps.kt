package uk.co.grahamcox.muck.service.acceptance.authentication

import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import io.cucumber.datatable.DataTable
import uk.co.grahamcox.muck.service.acceptance.requester.ListResponseMatcher
import uk.co.grahamcox.muck.service.acceptance.requester.Requester

/**
 * Cucumber steps for authentication
 */
class AuthenticationSteps(
        private val requester: Requester,
        private val authenticationServicesMatcher: ListResponseMatcher
) {
    @When("^I get the list of active authentication services$")
    fun checkHealth() {
        requester.get("/api/authentication/external")
    }

    @Then("^the returned authentication services are:$")
    fun expectAuthenticationServices(expected: DataTable) {
        val expectedMaps = expected.transpose().asMaps()

        authenticationServicesMatcher.match(expectedMaps)
    }
}
