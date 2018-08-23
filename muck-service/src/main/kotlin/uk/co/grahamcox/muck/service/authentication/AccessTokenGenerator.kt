package uk.co.grahamcox.muck.service.authentication

import uk.co.grahamcox.muck.service.user.UserId

/**
 * Mechanism to generate an Access Token
 */
interface AccessTokenGenerator {
    /**
     * Generate an Access Token for the user
     * @param user The user to generate an access token for
     * @return the access token
     */
    fun generate(user: UserId) : AccessToken
}
