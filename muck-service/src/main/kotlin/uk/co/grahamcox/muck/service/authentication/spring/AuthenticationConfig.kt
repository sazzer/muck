package uk.co.grahamcox.muck.service.authentication.spring

import com.auth0.jwt.algorithms.Algorithm
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.service.authentication.AccessTokenGeneratorImpl
import uk.co.grahamcox.muck.service.authentication.AccessTokenInterceptor
import uk.co.grahamcox.muck.service.authentication.AccessTokenStore
import uk.co.grahamcox.muck.service.authentication.JwtAccessTokenSerializerImpl

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
        }.initialize(context)
    }
}
