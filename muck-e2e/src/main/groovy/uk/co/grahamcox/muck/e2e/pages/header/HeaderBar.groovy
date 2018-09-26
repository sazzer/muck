package uk.co.grahamcox.muck.e2e.pages.header

import geb.Module
import geb.error.RequiredPageContentNotPresent
import org.awaitility.Awaitility

import java.util.concurrent.TimeUnit

/**
 * Module representing the header bar
 */
class HeaderBar extends Module {
    static content = {
        userMenu {
            Awaitility.waitAtMost(1, TimeUnit.SECONDS)
                    .pollDelay(100, TimeUnit.MILLISECONDS)
                    .ignoreException(RequiredPageContentNotPresent)
                    .until { !$("[data-test='user-menu']").toList().isEmpty() }

            $("[data-test='user-menu']").module(UserMenu)
        }
    }
}
