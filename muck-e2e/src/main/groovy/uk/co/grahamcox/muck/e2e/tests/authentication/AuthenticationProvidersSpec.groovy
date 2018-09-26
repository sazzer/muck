package uk.co.grahamcox.muck.e2e.tests.authentication

import uk.co.grahamcox.muck.e2e.E2eSpecBase
import uk.co.grahamcox.muck.e2e.pages.home.HomePage

class AuthenticationProvidersSpec extends E2eSpecBase {
    def "Check the list of Authentication Providers"() {
        when:
        to HomePage

        then:
        socialLoginButtons.getProviders() == ["google"]
    }
}
