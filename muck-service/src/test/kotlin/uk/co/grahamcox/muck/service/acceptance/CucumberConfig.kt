package uk.co.grahamcox.muck.service.acceptance

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

/**
 * Base spring configuration for all of the acceptance tests
 */
@Configuration
@Import(
)
class CucumberConfig(context: GenericApplicationContext) {
    init {
        beans {
        }.initialize(context)
    }
}
