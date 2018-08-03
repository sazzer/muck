package uk.co.grahamcox.muck.service.acceptance

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.service.acceptance.requester.RequesterConfig

/**
 * Base spring configuration for all of the acceptance tests
 */
@Configuration
@Import(
        RequesterConfig::class
)
class CucumberConfig(context: GenericApplicationContext) {
    init {
        beans {
        }.initialize(context)
    }
}
