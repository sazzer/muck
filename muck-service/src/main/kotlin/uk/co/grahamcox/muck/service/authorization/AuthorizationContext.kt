package uk.co.grahamcox.muck.service.authorization

import uk.co.grahamcox.muck.service.authentication.AccessToken
import uk.co.grahamcox.muck.service.user.UserId

/**
 * The Authorization Context in which to perform Authorization calls
 */
class AuthorizationContext(private val accessToken: AccessToken) {
    /**
     * Check if the current user is the same as the one provided
     * @param user The user to check for
     */
    fun isUser(user: UserId) {
        if (user != accessToken.user) {
            throw AuthorizationFailedException()
        }
    }
}
