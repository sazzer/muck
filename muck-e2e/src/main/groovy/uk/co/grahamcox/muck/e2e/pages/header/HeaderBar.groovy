package uk.co.grahamcox.muck.e2e.pages.header

import geb.Module

/**
 * Module representing the header bar
 */
class HeaderBar extends Module {
    static content = {
        userMenu {
            waitFor { $("[data-test='user-menu']").module(UserMenu) }
        }
    }
}
