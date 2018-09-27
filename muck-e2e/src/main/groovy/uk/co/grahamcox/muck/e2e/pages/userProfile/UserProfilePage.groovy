package uk.co.grahamcox.muck.e2e.pages.userProfile

import uk.co.grahamcox.muck.e2e.pages.BasePage

/**
 * The page object for the user profile page
 */
class UserProfilePage extends BasePage {
    static at = {
        $("div[data-test='user-profile-page']").displayed
    }

    static content = {
        userProfileMenu { $("div.vertical.tabular.menu a:nth-child(1)") }
        loginsMenu { $("div.vertical.tabular.menu a:nth-child(2)") }

        userProfileArea {
            userProfileMenu.click()
            $("form[data-test='user-profile-data']").module(UserProfileArea)
        }
    }
}
