package uk.co.grahamcox.muck.e2e.browser

import org.awaitility.Awaitility
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebElement
import java.util.concurrent.TimeUnit

/**
 * Wait until a selector returns that something is present
 */
fun WebElement.waitUntilPresent(selector: By): WebElement {
    await()
            .until {
                this.findElements(selector).isNotEmpty()
            }
    return this
}

/**
 * Wait until this element is present
 */
fun WebElement.waitUntilVisible(): WebElement {
    await()
            .until {
                this.isDisplayed
            }
    return this
}

/**
 * Build the standard Awaitility setup for waiting for elements
 */
private fun await() = Awaitility.await()
        .atMost(5, TimeUnit.SECONDS)
        .pollInterval(100, TimeUnit.MILLISECONDS)
        .ignoreException(NoSuchElementException::class.java)

/**
 * Find some elements, waiting until they are present first
 */
fun WebElement.findElementsWhenPresent(selector: By): List<WebElement> {
    waitUntilPresent(selector)
    return findElements(selector)
}
