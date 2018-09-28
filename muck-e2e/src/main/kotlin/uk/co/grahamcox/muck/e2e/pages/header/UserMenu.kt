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

    /** The name of the current logged-in user */
    val currentUserName: String
    get() = currentUserNameElement.text
}
