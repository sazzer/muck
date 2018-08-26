package uk.co.grahamcox.muck.service.authentication.spring

import com.auth0.jwt.algorithms.Algorithm
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.service.authentication.AccessTokenGeneratorImpl
import uk.co.grahamcox.muck.service.authentication.rest.AccessTokenArgumentResolver
import uk.co.grahamcox.muck.service.authentication.rest.AccessTokenInterceptor
import uk.co.grahamcox.muck.service.authentication.rest.AccessTokenStore
import uk.co.grahamcox.muck.service.authentication.rest.JwtAccessTokenSerializerImpl

/**
 * Spring configuration for working with Authentication
 */
@Configuration
class AuthenticationConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<AccessTokenGeneratorImpl>()
            bean {
                JwtAccessTokenSerializerImpl(Algorithm.HMAC512("MySecret"))
            }
            bean<AccessTokenStore>()
            bean<AccessTokenInterceptor>()
            bean<AccessTokenArgumentResolver>()
        }.initialize(context)
    }
}
