package uk.co.grahamcox.muck.e2e.pages

import geb.Page
import uk.co.grahamcox.muck.e2e.pages.header.HeaderBar

/**
 * Base page model that all the actual pages inherit from
 */
class BasePage extends Page {
    static content = {
        /** The module representing the page header */
        headerBar { $("[data-test='header-bar']").module(HeaderBar) }
    }
}
