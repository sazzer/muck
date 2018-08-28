package uk.co.grahamcox.muck.service.authentication.external.google

import org.slf4j.LoggerFactory
import org.springframework.web.util.UriComponentsBuilder
import uk.co.grahamcox.muck.service.authentication.external.AuthenticationRedirectBuilder
import java.net.URI
import java.util.*

/**
 * Implementation of the [AuthenticationRedirectBuilder] for redirecting to Google
 */
class GoogleAuthenticationRedirectBuilderImpl(
        private val authUri: URI,
        private val clientId: String,
        private val redirectUri: URI
) : AuthenticationRedirectBuilder {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(GoogleAuthenticationRedirectBuilderImpl::class.java)
    }

    /**
     * Build the URI to redirect the user to
     * @return the URI to redirect the user to
     */
    override fun build(): URI {
        val result = UriComponentsBuilder.fromUri(authUri)
                .queryParam("client_id", clientId)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid email profile")
                .queryParam("redirect_uri", redirectUri)
                .queryParam("state", UUID.randomUUID().toString())
                .build()
                .toUri()
        LOG.debug("Built redirect URI: {}", result)
        return result
    }
}
