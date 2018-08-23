package uk.co.grahamcox.muck.service.authentication

import uk.co.grahamcox.muck.service.user.UserId
import java.time.Instant

/**
 * The Access Token to use for the request
 * @property id The ID of this Access Token
 * @property user The User that this Access Token is for
 * @property created When the Access Token was created
 * @property expires When the Access Token expires
 */
data class AccessToken(
        val id: AccessTokenId,
        val user: UserId,
        val created: Instant,
        val expires: Instant
)
