package uk.co.grahamcox.muck.e2e.authentication

import cucumber.api.java.en.Then
import org.junit.Assert
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.muck.e2e.browser.Browser
import uk.co.grahamcox.muck.e2e.pages.home.HomePage

/**
 * Cucumber steps for Authentication
 */
class AuthenticationSteps {
    /** The browser */
    @Autowired
    private lateinit var browser: Browser

    /**
     * Check the supported authentication providers
     */
    @Then("^the supported authentication providers are:$")
    fun checkAuthenticationProviders(expected: List<String>) {
        val homePage: HomePage = browser.getPage(::HomePage)
        Assert.assertEquals(expected, homePage.socialLoginButtons.socialLoginButtonNames)
    }
}
