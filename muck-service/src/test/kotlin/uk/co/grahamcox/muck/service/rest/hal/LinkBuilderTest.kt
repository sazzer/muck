package uk.co.grahamcox.muck.service.rest.hal

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.co.grahamcox.muck.service.CurrentRequest
import uk.co.grahamcox.muck.service.authentication.external.rest.ExternalAuthenticationController
import uk.co.grahamcox.muck.service.home.rest.HomeController

/**
 * Unit tests for the Link Builder
 */
@CurrentRequest("http://localhost:8080/api")
internal class LinkBuilderTest {
    /**
     * Test a link that has no parameters
     */
    @Test
    fun buildLinkWithoutParams() {
        val link = HomeController::getHomeDocument.buildUri()
        Assertions.assertEquals("http://localhost:8080/api/", link)
    }

    /**
     * Test a link that has parameters with fixed values
     */
    @Test
    fun buildLinkWithParams() {
        val link = ExternalAuthenticationController::startAuthentication.buildUri(mapOf(
                "service" to "google")
        )
        Assertions.assertEquals("http://localhost:8080/api/authentication/external/google/start", link)
    }

    /**
     * Test a link that has parameters with fixed values
     */
    @Test
    fun buildLinkWithMissingParams() {
        val link = ExternalAuthenticationController::startAuthentication.buildUri(emptyMap())
        Assertions.assertEquals("http://localhost:8080/api/authentication/external/{service}/start", link)
    }
}
