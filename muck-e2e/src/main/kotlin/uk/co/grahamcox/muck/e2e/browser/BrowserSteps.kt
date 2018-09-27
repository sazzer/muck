package uk.co.grahamcox.muck.e2e.browser

import cucumber.api.Scenario
import cucumber.api.java.After
import cucumber.api.java.en.Given
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import java.io.File
import java.nio.file.Files

/**
 * Cucumber steps for working with the browser
 */
class BrowserSteps {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(BrowserSteps::class.java)
    }

    /** The browser */
    @Autowired
    private lateinit var browser: Browser

    /** Where to save screenshots */
    @Value("\${dir.screenshot}")
    private lateinit var screenshotPath: String

    /**
     * Open the home page
     */
    @Given("^I view the home page$")
    fun openHomePage() {
        browser.openUrl("/")
    }

    /**
     * Take a screenshot after every scenario
     */
    @After
    fun screenshot(scenario: Scenario) {
        if (screenshotPath.isNotBlank()) {
            val featurePath = scenario.uri.replace("^.*uk/co/grahamcox".toRegex(), "uk/co/grahamcox")

            LOG.debug("Screenshot after scenario: {} of Feature {}", scenario.name, featurePath)

            val targetDir = File(listOf(screenshotPath, featurePath).joinToString(File.separator))
            targetDir.mkdirs()

            val screenshotFile = File(listOf(screenshotPath, featurePath, scenario.name + ".png").joinToString(File.separator))
            LOG.debug("Writing screenshot to file: {}",screenshotFile.absolutePath)
            Files.write(screenshotFile.toPath(), browser.screenshot())

            val contentFile = File(listOf(screenshotPath, featurePath, scenario.name + ".html").joinToString(File.separator))
            LOG.debug("Writing page content to file: {}",screenshotFile.absolutePath)
            Files.write(contentFile.toPath(), browser.content().toByteArray(Charsets.UTF_8))
        }
    }
}
