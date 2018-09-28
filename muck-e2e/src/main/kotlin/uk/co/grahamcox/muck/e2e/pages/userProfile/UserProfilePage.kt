package uk.co.grahamcox.muck.e2e.pages.userProfile

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory
import uk.co.grahamcox.muck.e2e.browser.waitUntilVisible
import uk.co.grahamcox.muck.e2e.pages.PageBase

/**
 * The page model for the user profile page
 */
class UserProfilePage(pageBase: WebElement) : PageBase() {
    init {
        PageFactory.initElements(DefaultElementLocatorFactory(pageBase), this)
    }

    /** The element representing the user data form */
    @FindBy(css = "[data-test='user-profile-data']")
    private lateinit var userProfileDataElement: WebElement

    /** The user profile data area */
    val userProfileData: UserProfileData
    get() {
        userProfileDataElement.waitUntilVisible()
        return UserProfileData(userProfileDataElement)
    }
}
