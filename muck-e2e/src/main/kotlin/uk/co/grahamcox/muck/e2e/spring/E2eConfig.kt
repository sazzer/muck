package uk.co.grahamcox.muck.e2e.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource
import uk.co.grahamcox.muck.e2e.browser.BrowserConfig
import uk.co.grahamcox.muck.e2e.userProfile.UserProfileConfig

/**
 * The root of the Spring configuration for the E2E tests
 */
@Configuration
@Import(
        BrowserConfig::class,
        UserProfileConfig::class
)
@PropertySource("classpath:/application.properties")
class E2eConfig
