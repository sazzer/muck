package uk.co.grahamcox.muck.e2e.pages.home

import geb.Module

/**
 * Module representing the social login buttons
 */
class SocialLoginButtons extends Module {
    static content = {
        /** The actual buttons */
        buttons {
            waitFor {
                $("button")
            }
        }

        button { id -> $("button[data-test='social-login-button-$id']")}
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

    /**
     * Log in with the given provider
     * @param provider the ID of the provider
     */
    def loginWith(provider) {
        button(provider).click()
    }
}
