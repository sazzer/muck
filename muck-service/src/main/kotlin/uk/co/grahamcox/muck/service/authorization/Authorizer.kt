package uk.co.grahamcox.muck.service.authorization

import uk.co.grahamcox.muck.service.authentication.AccessToken

/**
 * Mechanism by which we can authorize a request
 */
class Authorizer(private val accessToken: AccessToken) {
    /**
     * Trigger the provided lambda in the correct way to perform authorization
     * @param block The block to trigger
     */
    operator fun invoke(block: AuthorizationContext.() -> Unit) {
        val context = AuthorizationContext(accessToken)
        context.block()
    }

}
