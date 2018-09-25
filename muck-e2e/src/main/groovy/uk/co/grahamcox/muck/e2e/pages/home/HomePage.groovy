package uk.co.grahamcox.muck.e2e.pages.home

import geb.Page

/**
 * Page model for the home page of the application
 */
class HomePage extends Page {
    /** The URL to go to */
    static url = "/"

    static content = {
        /** The module representing the social login buttons */
        socialLoginButtons { $("[data-test='social-login-buttons']").module(SocialLoginButtons) }
    }
}
