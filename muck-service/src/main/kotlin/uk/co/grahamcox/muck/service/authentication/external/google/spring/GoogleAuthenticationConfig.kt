package uk.co.grahamcox.muck.service.authentication.external.google.spring

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.core.env.get
import uk.co.grahamcox.muck.service.authentication.external.AuthenticationServiceImpl
import uk.co.grahamcox.muck.service.authentication.external.google.GoogleAuthenticationRedirectBuilderImpl
import java.net.URI

/**
 * Spring configuration for the Google Authentication Provider
 */
@Configuration
@ConditionalOnProperty("muck.authentication.google.enabled")
class GoogleAuthenticationConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean("googleAuthenticationService") {
                val authUri = env["muck.authentication.google.authUri"]
                val clientId = env["muck.authentication.google.clientId"]
                val redirectUri = env["muck.authentication.google.redirectUri"]

                AuthenticationServiceImpl(
                        redirectBuilder = GoogleAuthenticationRedirectBuilderImpl(
                                URI(authUri),
                                clientId,
                                URI(redirectUri)
                        )
                )
            }
        }.initialize(context)
    }
}
