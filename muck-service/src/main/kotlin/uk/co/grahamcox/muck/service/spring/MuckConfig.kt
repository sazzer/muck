package uk.co.grahamcox.muck.service.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.service.authentication.external.spring.ExternalAuthenticationConfig
import uk.co.grahamcox.muck.service.authentication.spring.AuthenticationConfig
import uk.co.grahamcox.muck.service.database.DatabaseConfig
import uk.co.grahamcox.muck.service.rest.ProblemResponseBodyAdvice
import uk.co.grahamcox.muck.service.user.spring.UserConfig
import java.time.Clock

/**
 * Core configuration of MucK
 */
@Configuration
@Import(
        DatabaseConfig::class,
        UserConfig::class,
        AuthenticationConfig::class,
        ExternalAuthenticationConfig::class,
        WebMvcConfig::class
)
class MuckConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean { Clock.systemUTC() }
            bean<ProblemResponseBodyAdvice>()
        }.initialize(context)
    }
}
