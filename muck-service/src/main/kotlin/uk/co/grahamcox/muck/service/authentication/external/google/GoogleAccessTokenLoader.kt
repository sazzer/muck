package uk.co.grahamcox.muck.service.authentication.external.google

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.net.URI

/**
 * Mechanism to load the access token details from Google
 */
class GoogleAccessTokenLoader(
        private val restTemplate: RestTemplate,
        private val tokenUri: URI,
        private val clientId: String,
        private val clientSecret: String,
        private val redirectUri: URI
) {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(GoogleAccessTokenLoader::class.java)
    }

    /**
     * Load the remote user that the given login input described
     */
    fun retrieveAccessToken(authCode: String): GoogleAccessToken {
        LOG.debug("Retrieving access token for authorization code {}", authCode)

        val params = mapOf(
                "code" to authCode,
                "client_id" to clientId,
                "client_secret" to clientSecret,
                "redirect_uri" to redirectUri.toString(),
                "grant_type" to "authorization_code"
        ).mapValues { listOf(it.value) }

        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val googleAccessToken = restTemplate.exchange(RequestEntity(
                LinkedMultiValueMap(params),
                requestHeaders,
                HttpMethod.POST,
                tokenUri
        ), GoogleAccessToken::class.java)
        LOG.debug("Retrieved access token: {}", googleAccessToken)

        return googleAccessToken.body ?: throw IllegalStateException("Failed to get access token")
    }
}
