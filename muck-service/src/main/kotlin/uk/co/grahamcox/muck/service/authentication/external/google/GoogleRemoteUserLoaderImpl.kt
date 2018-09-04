package uk.co.grahamcox.muck.service.authentication.external.google

import com.auth0.jwt.JWT
import org.slf4j.LoggerFactory
import uk.co.grahamcox.muck.service.authentication.external.RemoteUser
import uk.co.grahamcox.muck.service.authentication.external.RemoteUserLoader

/**
 * Mechanism to load the remote user details from Google
 */
class GoogleRemoteUserLoaderImpl(
        private val accessTokenLoader: GoogleAccessTokenLoader
) : RemoteUserLoader {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(GoogleRemoteUserLoaderImpl::class.java)
    }

    /**
     * Load the remote user that the given login input described
     */
    override fun loadRemoteUser(input: Map<String, String>): RemoteUser {
        val authCode = input["code"] ?: throw IllegalArgumentException("Missing required parameter: code")

        val accessToken = accessTokenLoader.retrieveAccessToken(authCode)

        LOG.debug("Decoding ID Token: {}", accessToken.idToken)
        val jwt = JWT.decode(accessToken.idToken)
        LOG.debug("Parsed ID Token {}: {}", accessToken.idToken, jwt)

        val googleId = jwt.subject
        val email = jwt.getClaim("email").asString()
        val displayName = jwt.getClaim("name").asString()

        val result = RemoteUser(
                providerId = googleId,
                providerDisplayName = email,
                email = email,
                displayName = displayName
        )

        LOG.debug("Loaded remote user details: {}", result)
        return result
    }
}
