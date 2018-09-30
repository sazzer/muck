package uk.co.grahamcox.muck.e2e.pages

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import uk.co.grahamcox.muck.e2e.browser.Page
import uk.co.grahamcox.muck.e2e.pages.header.HeaderBar

/**
 * Base class for all pages, including common elements
 */
abstract class PageBase : Page {
    /** Web Element for the page header */
    @FindBy(css = "[data-test='header-bar']")
    private lateinit var headerBarElement: WebElement

    /** The page header */
    val headerBar: HeaderBar
    get() = HeaderBar(headerBarElement)
}
