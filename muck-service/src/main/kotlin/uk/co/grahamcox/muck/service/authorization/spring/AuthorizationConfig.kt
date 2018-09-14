package uk.co.grahamcox.muck.service.authorization.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.service.authorization.rest.AuthorizationControllerAdvice

/**
 * Spring Config for Authorization
 */
@Configuration
class AuthorizationConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<AuthorizationControllerAdvice>()
        }.initialize(context)
    }
}
