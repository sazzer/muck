package uk.co.grahamcox.muck.e2e.tests.authentication

import geb.spock.GebReportingSpec
import uk.co.grahamcox.muck.e2e.pages.home.HomePage

class AuthenticationProvidersSpec extends GebReportingSpec {
    def "Check the list of Authentication Providers"() {
        when:
        to HomePage

        then:
        socialLoginButtons.getProviders() == ["google"]
    }
}
