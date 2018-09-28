package uk.co.grahamcox.muck.e2e.browser

import org.awaitility.Awaitility
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import java.util.concurrent.TimeUnit

/**
 * Wait until a selector returns that something is present
 */
fun WebElement.waitUntilPresent(selector: By): WebElement {
    Awaitility.await()
            .atMost(1, TimeUnit.SECONDS)
            .pollInterval(100, TimeUnit.MILLISECONDS)
            .until {
                this.findElements(selector).isNotEmpty()
            }
    return this
}

/**
 * Wait until this element is present
 */
fun WebElement.waitUntilVisible(): WebElement {
    Awaitility.await()
            .atMost(1, TimeUnit.SECONDS)
            .pollInterval(100, TimeUnit.MILLISECONDS)
            .until {
                this.isDisplayed
            }
    return this
}

/**
 * Find some elements, waiting until they are present first
 */
fun WebElement.findElementsWhenPresent(selector: By): List<WebElement> {
    waitUntilPresent(selector)
    return findElements(selector)
}
