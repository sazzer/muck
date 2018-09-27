package uk.co.grahamcox.muck.e2e.pages.userProfile

import geb.Module

/**
 * Module for the User Profile Area of the User Profile Page
 */
class UserProfileArea extends Module {
    static content = {
        displayNameInput { $("input[name='displayName']") }
        emailInput { $("input[name='email']") }

        displayName { displayNameInput.value() }
        email { emailInput.value() }
    }
}
