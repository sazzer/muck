import org.openqa.selenium.Platform
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver

reportsDir = "target/geb"

driver = "chrome"

baseUrl = System.getProperty("baseUrl", "http://localhost:8000")

environments {
    remote {
        driver = {
            def seleniumUrl = System.getProperty("selenium.url")
            def browserName = System.getProperty("browserName")

            new RemoteWebDriver(new URL(seleniumUrl), new DesiredCapabilities(browserName, "", Platform.ANY))
        }
    }
}
