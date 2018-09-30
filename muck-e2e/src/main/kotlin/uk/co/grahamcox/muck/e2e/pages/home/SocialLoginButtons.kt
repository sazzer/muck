package uk.co.grahamcox.muck.e2e.pages.home

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import uk.co.grahamcox.muck.e2e.browser.findElementsWhenPresent

/**
 * The page model for the Social Login Buttons
 */
class SocialLoginButtons(private val pageBase: WebElement) {
    /**
     * Authenticate using the specified authentication provider
     */
    fun authenticateAs(provider: String) {
        val button = socialLoginButtons.find { it.getAttribute("data-test") == "social-login-button-$provider" }
            ?: throw IllegalArgumentException("Social login button $provider not found")
        button.click()
    }

    private val socialLoginButtons: List<WebElement>
        get() {
            return pageBase.findElementsWhenPresent(By.cssSelector("button[data-test]"))
        }

    /**
     * Get the names of the social login buttons
     */
    val socialLoginButtonNames: List<String>
        get() {
            return socialLoginButtons
                    .mapNotNull { it.getAttribute("data-test") }
                    .filter { it.startsWith("social-login-button-") }
                    .map { it.removePrefix("social-login-button-") }
        }
}
