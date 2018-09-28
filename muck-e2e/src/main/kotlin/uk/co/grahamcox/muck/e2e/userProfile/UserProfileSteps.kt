package uk.co.grahamcox.muck.e2e.userProfile

import cucumber.api.java.en.When
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.muck.e2e.browser.Browser
import uk.co.grahamcox.muck.e2e.pages.home.HomePage

/**
 * Cucumber steps for working with user profiles
 */
class UserProfileSteps {
    /** The browser */
    @Autowired
    private lateinit var browser: Browser

    @When("^I view the user profile$")
    fun viewUserProfile() {
        val homePage = browser.getPage(::HomePage)
        homePage.headerBar.userMenu.goToUserProfile()
    }
}
