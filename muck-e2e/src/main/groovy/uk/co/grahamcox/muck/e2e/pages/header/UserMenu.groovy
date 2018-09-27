package uk.co.grahamcox.muck.e2e.pages.header

import geb.Module
import uk.co.grahamcox.muck.e2e.pages.userProfile.UserProfilePage

/**
 * Module to represent the User Menu in the header bar
 */
class UserMenu extends Module {
    static content = {
        userMenu { $("div.menu") }
        userMenuAnchor { $("div.text") }
        userName { userMenuAnchor.text() }

        userProfileLink(to: UserProfilePage) { $("div[data-test='user-profile']") }
    }

    def open() {
        if (!userMenu.classes().contains("visible")) {
            userMenuAnchor.click()
        }
    }

    def visitUserProfile() {
        open()
        userProfileLink.click()
    }
}
