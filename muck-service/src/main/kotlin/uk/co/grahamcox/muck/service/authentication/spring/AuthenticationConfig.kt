package uk.co.grahamcox.muck.service.authentication.spring

import com.auth0.jwt.algorithms.Algorithm
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.service.authentication.AccessTokenGeneratorImpl
import uk.co.grahamcox.muck.service.authentication.rest.*
import java.time.Duration

/**
 * Spring configuration for working with Authentication
 */
@Configuration
class AuthenticationConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean {
                AccessTokenGeneratorImpl(
                        ref(),
                        Duration.ofDays(365)
                )
            }
            bean {
                JwtAccessTokenSerializerImpl(Algorithm.HMAC512("MySecret"))
            }
            bean<AccessTokenStore>()
            bean<AccessTokenInterceptor>()
            bean<AccessTokenArgumentResolver>()
            bean<AuthenticationControllerAdvice>()

            bean<DebugController>()
        }.initialize(context)
    }
}
