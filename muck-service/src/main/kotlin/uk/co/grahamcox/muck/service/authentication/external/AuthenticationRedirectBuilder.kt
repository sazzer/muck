package uk.co.grahamcox.muck.service.authentication.external

import java.net.URI

/**
 * Strategy to build the URI to redirect the user to in order to start authentication
 */
interface AuthenticationRedirectBuilder {
    /**
     * Build the URI to redirect the user to
     * @return the URI to redirect the user to
     */
    fun build(): URI
}
