package uk.co.grahamcox.muck.e2e.browser

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.RemoteWebDriver
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import kotlin.reflect.KFunction

/**
 * Spring config for the web browser interaction
 */
@Configuration
class BrowserConfig(context: GenericApplicationContext) {
    /** The means to build browser drivers by name */
    val browserBuilders = mapOf<String, KFunction<RemoteWebDriver>>(
            "chrome" to ::ChromeDriver,
            "firefox" to ::FirefoxDriver
    )


    init {
        beans {
            bean {
                val seleniumUrl = env.getProperty("url.selenium")
                val browserName = env.getProperty("browserName")
                val baseUrl = env.getProperty("url.base") ?: throw IllegalArgumentException("No base URL provided")

                val driver = if (seleniumUrl.isNullOrBlank()) {
                    browserBuilders[browserName]?.call() ?: throw IllegalArgumentException("Unknown browser: $browserName")
                } else {
                    RemoteWebDriver.builder()
                            .url(seleniumUrl)
                            .setCapability("browserName", browserName)
                            .build() as RemoteWebDriver
                }

                Browser(driver, baseUrl)
            }
        }.initialize(context)
    }
}
