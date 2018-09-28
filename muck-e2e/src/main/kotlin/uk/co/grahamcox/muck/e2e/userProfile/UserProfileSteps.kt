package uk.co.grahamcox.muck.e2e.userProfile

import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.muck.e2e.browser.Browser
import uk.co.grahamcox.muck.e2e.matcher.BeanField
import uk.co.grahamcox.muck.e2e.matcher.BeanMatcher
import uk.co.grahamcox.muck.e2e.pages.home.HomePage
import uk.co.grahamcox.muck.e2e.pages.userProfile.UserProfileData
import uk.co.grahamcox.muck.e2e.pages.userProfile.UserProfilePage

/**
 * Cucumber steps for working with user profiles
 */
class UserProfileSteps {
    /** The browser */
    @Autowired
    private lateinit var browser: Browser

    /** The matcher to use for matching User Profile Details */
    @Autowired
    private lateinit var userProfileDetailsMatcher: BeanMatcher<UserProfileData>

    @When("^I view the user profile$")
    fun viewUserProfile() {
        val homePage = browser.getPage(::HomePage)
        homePage.headerBar.userMenu.goToUserProfile()
    }

    @Then("^the user profile details are:$")
    fun compareUserProfileData(expected: Map<String, String>) {
        val userProfile = browser.getPage(::UserProfilePage)
        val userProfileDetails = userProfile.userProfileData

        userProfileDetailsMatcher.match(userProfileDetails, expected)
    }
}
