package uk.co.grahamcox.muck.service.authentication.external.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import uk.co.grahamcox.muck.service.authentication.external.google.spring.GoogleAuthenticationConfig

/**
 * Spring configuration for the External Authentication Providers
 */
@Configuration
@Import(
        GoogleAuthenticationConfig::class
)
class ExternalAuthenticationConfig
