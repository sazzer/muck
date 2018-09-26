package uk.co.grahamcox.muck.e2e.tests.authentication

import uk.co.grahamcox.muck.e2e.E2eSpecBase
import uk.co.grahamcox.muck.e2e.pages.home.HomePage

/**
 * End-to-end tests for authenticating with Google
 */
class GoogleAuthenticationSpec extends E2eSpecBase {
    def "Authenticating with Google"() {
        given:
        to HomePage

        when:
        socialLoginButtons.loginWith("google")

        then:
        headerBar.userMenu.userName == "Test User"
    }
}
