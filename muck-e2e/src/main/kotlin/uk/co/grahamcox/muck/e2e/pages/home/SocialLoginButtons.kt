package uk.co.grahamcox.muck.e2e.pages.home

import org.awaitility.Awaitility
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import java.util.concurrent.TimeUnit

/**
 * The page model for the Social Login Buttons
 */
class SocialLoginButtons(private val pageBase: WebElement) {
    /**
     * Get the names of the social login buttons
     */
    val socialLoginButtonNames: List<String>
        get() {
            Awaitility.await()
                    .atMost(1, TimeUnit.SECONDS)
                    .pollInterval(100, TimeUnit.MILLISECONDS)
                    .until {
                        val buttons = pageBase.findElements(By.cssSelector("button"))
                        buttons.isNotEmpty()
                    }

            return pageBase.findElements(By.cssSelector("button"))
                    .mapNotNull { it.getAttribute("data-test") }
                    .filter { it.startsWith("social-login-button-") }
                    .map { it.removePrefix("social-login-button-") }
        }
}
