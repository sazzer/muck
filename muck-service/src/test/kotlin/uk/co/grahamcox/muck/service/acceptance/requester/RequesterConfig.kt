package uk.co.grahamcox.muck.service.acceptance.requester

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

/**
 * Spring Config for standard request behaviours
 */
@Configuration
class RequesterConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<Requester>()
        }.initialize(context)
    }
}
