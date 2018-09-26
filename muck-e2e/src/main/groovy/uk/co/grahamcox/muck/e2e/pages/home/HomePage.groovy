package uk.co.grahamcox.muck.e2e.pages.home

import uk.co.grahamcox.muck.e2e.pages.BasePage

/**
 * Page model for the home page of the application
 */
class HomePage extends BasePage {
    /** The URL to go to */
    static url = "/"

    static content = {
        /** The module representing the social login buttons */
        socialLoginButtons { $("[data-test='social-login-buttons']").module(SocialLoginButtons) }
    }
}
