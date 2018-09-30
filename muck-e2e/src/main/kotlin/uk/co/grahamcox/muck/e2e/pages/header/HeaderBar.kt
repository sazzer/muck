package uk.co.grahamcox.muck.e2e.pages.header

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory
import uk.co.grahamcox.muck.e2e.browser.waitUntilVisible

/**
 * The header bar on every page
 */
class HeaderBar(pageBase: WebElement) {
    init {
        PageFactory.initElements(DefaultElementLocatorFactory(pageBase), this)
    }

    /** The web element for the user menu */
    @FindBy(css = "[data-test='user-menu']")
    private lateinit var userMenuElement: WebElement

    /** The user menu. Might not be present */
    val userMenu: UserMenu
    get() {
        userMenuElement.waitUntilVisible()
        return UserMenu(userMenuElement)
    }
}
