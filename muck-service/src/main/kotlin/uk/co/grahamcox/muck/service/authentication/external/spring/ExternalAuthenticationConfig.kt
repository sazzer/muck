package uk.co.grahamcox.muck.service.authentication.external.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.service.authentication.external.google.spring.GoogleAuthenticationConfig
import uk.co.grahamcox.muck.service.authentication.external.rest.ExternalAuthenticationController

/**
 * Spring configuration for the External Authentication Providers
 */
@Configuration
@Import(
        GoogleAuthenticationConfig::class
)
class ExternalAuthenticationConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<ExternalAuthenticationController>()
        }.initialize(context)
    }
}
