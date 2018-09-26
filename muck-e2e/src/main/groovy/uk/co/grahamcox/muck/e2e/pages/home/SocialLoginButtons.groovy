package uk.co.grahamcox.muck.e2e.pages.home

import geb.Module
import geb.error.RequiredPageContentNotPresent
import org.awaitility.Awaitility

import java.util.concurrent.TimeUnit

/**
 * Module representing the social login buttons
 */
class SocialLoginButtons extends Module {
    static content = {
        /** The actual buttons */
        buttons { $("button") }

        button { id -> $("button[data-test='social-login-button-$id']")}
    }

    /**
     * Get the list of configured providers
     * @return the list of configured providers
     */
    def getProviders() {
        waitForButtons()

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
        waitForButtons()

        button(provider).click()
    }

    /**
     * Wait for the social login buttons to be rendered
     */
    private waitForButtons() {
        Awaitility.waitAtMost(1, TimeUnit.SECONDS)
                .pollDelay(100, TimeUnit.MILLISECONDS)
                .ignoreException(RequiredPageContentNotPresent)
                .until { !buttons.toList().isEmpty() }
    }
}
