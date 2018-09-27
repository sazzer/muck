package uk.co.grahamcox.muck.e2e.tests.userProfile

import uk.co.grahamcox.muck.e2e.E2eSpecBase
import uk.co.grahamcox.muck.e2e.pages.home.HomePage

/**
 * End-to-end tests for viewing the user profile
 */
class ViewUserProfileSpec extends E2eSpecBase {
    def "View User Profile Details"() {
        given:
        to HomePage
        socialLoginButtons.loginWith("google")

        when:
        headerBar.userMenu.visitUserProfile()

        then:
        userProfileArea.displayName == "Test User"
        userProfileArea.email == "test@example.com"
    }
}
