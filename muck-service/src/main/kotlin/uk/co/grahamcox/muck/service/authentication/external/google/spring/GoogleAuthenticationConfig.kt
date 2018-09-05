package uk.co.grahamcox.muck.service.authentication.external.google.spring

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.core.env.get
import uk.co.grahamcox.muck.service.authentication.external.AuthenticationServiceImpl
import uk.co.grahamcox.muck.service.authentication.external.google.GoogleAccessTokenLoader
import uk.co.grahamcox.muck.service.authentication.external.google.GoogleAuthenticationRedirectBuilderImpl
import uk.co.grahamcox.muck.service.authentication.external.google.GoogleRemoteUserLoaderImpl
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
                val authUri = URI(env["muck.authentication.google.authUri"])
                val tokenUri = URI(env["muck.authentication.google.tokenUri"])
                val clientId = env["muck.authentication.google.clientId"]
                val clientSecret = env["muck.authentication.google.clientSecret"]
                val redirectUri = URI(env["muck.authentication.google.redirectUri"])

                AuthenticationServiceImpl(
                        id = "google",
                        redirectBuilder = GoogleAuthenticationRedirectBuilderImpl(
                                authUri,
                                clientId,
                                redirectUri
                        ),
                        remoteUserLoader = GoogleRemoteUserLoaderImpl(
                                accessTokenLoader = GoogleAccessTokenLoader(
                                        restTemplate = ref(),
                                        redirectUri = redirectUri,
                                        tokenUri = tokenUri,
                                        clientId = clientId,
                                        clientSecret = clientSecret
                                )
                        ),
                        userService = ref()
                )
            }
        }.initialize(context)
    }
}
