package uk.co.grahamcox.muck.e2e.pages.userProfile

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory
import uk.co.grahamcox.muck.e2e.browser.waitUntilVisible
import uk.co.grahamcox.muck.e2e.pages.PageBase

/**
 * The page model for the user profile data area
 */
class UserProfileData(pageBase: WebElement) : PageBase() {
    init {
        PageFactory.initElements(DefaultElementLocatorFactory(pageBase), this)
    }

    /** Element for the display name */
    @FindBy(css = "input[name='displayName']")
    private lateinit var displayNameElement: WebElement

    /** Element for the email address */
    @FindBy(css = "input[name='email']")
    private lateinit var emailElement: WebElement

    /** The display name */
    val displayName: String
        get() {
            displayNameElement.waitUntilVisible()
            return displayNameElement.getAttribute("value")
        }

    /** The email address */
    val email: String
        get() {
            emailElement.waitUntilVisible()
            return emailElement.getAttribute("value")
        }
}
