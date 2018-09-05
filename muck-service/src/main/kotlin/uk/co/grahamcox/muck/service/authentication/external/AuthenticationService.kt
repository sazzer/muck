package uk.co.grahamcox.muck.service.authentication.external

import uk.co.grahamcox.muck.service.user.UserResource
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

    /**
     * Complete user authentication and return the user that has authenticated
     */
    fun completeAuthentication(input: Map<String, String>) : UserResource
}
