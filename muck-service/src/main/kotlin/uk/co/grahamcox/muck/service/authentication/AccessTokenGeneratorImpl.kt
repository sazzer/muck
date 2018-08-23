package uk.co.grahamcox.muck.service.authentication

import org.slf4j.LoggerFactory
import uk.co.grahamcox.muck.service.user.UserId
import java.time.Clock
import java.time.temporal.TemporalAmount
import java.util.*

/**
 * Standard implementation of the Access Token Generator
 */
class AccessTokenGeneratorImpl(
        private val clock: Clock,
        private val duration: TemporalAmount
) : AccessTokenGenerator {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(AccessTokenGeneratorImpl::class.java)
    }

    /**
     * Generate an Access Token for the user
     * @param user The user to generate an access token for
     * @return the access token
     */
    override fun generate(user: UserId): AccessToken {
        val now = clock.instant()
        val expires = now.plus(duration)
        val id = AccessTokenId(UUID.randomUUID().toString())

        val accessToken = AccessToken(
                id = id,
                user = user,
                created = now,
                expires = expires
        )
        LOG.debug("Generated access token: {}", accessToken)
        return accessToken

    }
}
