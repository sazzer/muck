package uk.co.grahamcox.muck.service.home.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.service.home.rest.HomeController

/**
 * Spring configuration for the Home Document
 */
@Configuration
class HomeConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<HomeController>()
        }.initialize(context)
    }
}
