package uk.co.grahamcox.muck.service.authentication.external.google.spring

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

/**
 * Spring configuration for the Google Authentication Provider
 */
@Configuration
@ConditionalOnProperty("muck.authentication.google.enabled")
class GoogleAuthenticationConfig(context: GenericApplicationContext) {
    init {
        beans {

        }.initialize(context)
    }
}
