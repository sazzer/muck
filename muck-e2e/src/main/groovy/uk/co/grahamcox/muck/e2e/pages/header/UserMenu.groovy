package uk.co.grahamcox.muck.e2e.pages.header

import geb.Module

/**
 * Module to represent the User Menu in the header bar
 */
class UserMenu extends Module {
    static content = {
        userName { $("div.text").text() }
    }
}
