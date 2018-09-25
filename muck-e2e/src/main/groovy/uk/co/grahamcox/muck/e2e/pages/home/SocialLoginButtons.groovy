package uk.co.grahamcox.muck.e2e.pages.home

import geb.Module

/**
 * Module representing the social login buttons
 */
class SocialLoginButtons extends Module {
    static content = {
        /** The actual buttons */
        buttons { $("button") }
    }

    /**
     * Get the list of configured providers
     * @return the list of configured providers
     */
    def getProviders() {
        return buttons.toList()
            .findAll { it.attr("data-test") != null }
            .collect { it.attr("data-test") }
            .findAll { it.startsWith("social-login-button-") }
            .collect { it.substring("social-login-buttons".length()) }
            .sort()
    }
}
