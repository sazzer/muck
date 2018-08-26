package uk.co.grahamcox.muck.service.authentication

/**
 * Interface describing how to get hold of the Access Token for the current request
 */
interface AccessTokenHolder {
    /** The access token */
    val accessToken: AccessToken?
}
