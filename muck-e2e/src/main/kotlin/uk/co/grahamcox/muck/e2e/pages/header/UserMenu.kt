package uk.co.grahamcox.muck.e2e.pages.header

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory

/**
 * The User Menu on the header bar
 */
class UserMenu(pageBase: WebElement) {
    init {
        PageFactory.initElements(DefaultElementLocatorFactory(pageBase), this)
    }

    /** The element containing the current user name */
    @FindBy(css = "div.text[role='alert']")
    private lateinit var currentUserNameElement: WebElement

    /** The element containing the actual menu */
    @FindBy(css = "div.menu.transition")
    private lateinit var menuContent: WebElement

    /** The element representing the user profile menu item */
    @FindBy(css = "[data-test='user-profile']")
    private lateinit var userProfileMenuItem: WebElement

    /** The name of the current logged-in user */
    val currentUserName: String
    get() = currentUserNameElement.text

    /**
     * Open the menu if it's not already open
     */
    fun openMenu() {
        if (!menuContent.isDisplayed) {
            currentUserNameElement.click()
        }
    }

    /**
     * Go to the user profile
     */
    fun goToUserProfile() {
        openMenu()
        userProfileMenuItem.click()
    }
}
