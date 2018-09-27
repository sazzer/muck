package uk.co.grahamcox.muck.e2e.browser

import org.openqa.selenium.OutputType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.web.util.UriComponentsBuilder

/**
 * Wrapper around the browser itself
 */
class Browser(
        private val webdriver: RemoteWebDriver,
        private val baseUrl: String) : InitializingBean, DisposableBean {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(Browser::class.java)
    }

    /**
     * Open up the browser onto the home page
     */
    override fun afterPropertiesSet() {
        openUrl("/")
    }

    /**
     * Destroy the browser when we've finished with it
     */
    override fun destroy() {
        webdriver.quit()
    }

    /**
     * Take a screenshot and return it as a byte array
     * @return the screenshot as bytes
     */
    fun screenshot(): ByteArray {
        return webdriver.getScreenshotAs(OutputType.BYTES)
    }

    /**
     * Get the page source
     * @return the page source
     */
    fun content(): String {
        return webdriver.pageSource
    }

    /**
     * Open the browser onto the given URL
     * @param url The URL to open
     */
    fun openUrl(url: String) {
        val target = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment(url)
                .build()
                .toUriString()

        LOG.debug("Opening URL {}", target)
        webdriver.get(target)
    }
}
