package uk.co.grahamcox.muck.service.authentication.external

import java.net.URI

/**
 * Standard strategy-based implementation of the Authentication Service
 */
class AuthenticationServiceImpl(
        override val id: String,
        private val redirectBuilder: AuthenticationRedirectBuilder
) : AuthenticationService {
    /**
     * Build the URI to redirect the user to
     * @return the URI to redirect the user to
     */
    override fun buildRedirectUri(): URI {
        return redirectBuilder.build()
    }
}
