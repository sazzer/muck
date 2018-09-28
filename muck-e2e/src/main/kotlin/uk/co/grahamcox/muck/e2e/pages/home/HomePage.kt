package uk.co.grahamcox.muck.e2e.pages.home

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory
import uk.co.grahamcox.muck.e2e.pages.PageBase

/**
 * The page model for the home page
 */
class HomePage(pageBase: WebElement) : PageBase() {
    init {
        PageFactory.initElements(DefaultElementLocatorFactory(pageBase), this)
    }

    /** The Web Element for the Social Login Buttons */
    @FindBy(css = "[data-test='social-login-buttons']")
    private lateinit var socialLoginButtonsElement: WebElement

    /**
     * Get the page model for the social login buttons
     */
    val socialLoginButtons: SocialLoginButtons
        get() {
            return SocialLoginButtons(socialLoginButtonsElement)
        }
}
