package uk.co.grahamcox.muck.service.authentication.rest

import uk.co.grahamcox.muck.service.authentication.AccessToken

/**
 * Interface describing how to get hold of the Access Token for the current request
 */
interface AccessTokenHolder {
    /** The access token */
    val accessToken: AccessToken?
}
