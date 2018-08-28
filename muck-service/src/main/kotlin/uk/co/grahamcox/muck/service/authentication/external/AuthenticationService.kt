package uk.co.grahamcox.muck.service.authentication.external

import java.net.URI

/**
 * Mechanism to facilitate authentication by a third-party service
 */
interface AuthenticationService {
    /** The identity of this Authentication Service */
    val id: String

    /**
     * Build the URI to redirect the user to
     * @return the URI to redirect the user to
     */
    fun buildRedirectUri(): URI

}
