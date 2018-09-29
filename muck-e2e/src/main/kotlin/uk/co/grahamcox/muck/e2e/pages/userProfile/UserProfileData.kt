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

    /** Element for the save button */
    @FindBy(css = "button.primary[type='submit']")
    private lateinit var saveElement: WebElement

    /** Element for the success message */
    @FindBy(css = "div.success.message")
    private lateinit var successElement: WebElement

    /** The display name */
    var displayName: String
        get() {
            displayNameElement.waitUntilVisible()
            return displayNameElement.getAttribute("value")
        }
        set(value: String) {
            displayNameElement.waitUntilVisible()
            displayNameElement.clear()
            displayNameElement.sendKeys(value)
        }

    /** The email address */
    var email: String
        get() {
            emailElement.waitUntilVisible()
            return emailElement.getAttribute("value")
        }
        set(value: String) {
            emailElement.waitUntilVisible()
            emailElement.clear()
            emailElement.sendKeys(value)
        }

    /** Whether the form is a success or not */
    val success: Boolean
        get() {
            return try {
                successElement.waitUntilVisible()
                true
            } catch (e: Exception) {
                false
            }
        }

    /**
     * Save the form
     */
    fun saveChanges() {
        saveElement.click()
    }
}
