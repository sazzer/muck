package uk.co.grahamcox.muck.service.authentication.external

/**
 * Interface to load the remote user details for the provided login input
 */
interface RemoteUserLoader {
    /**
     * Load the remote user that the given login input described
     */
    fun loadRemoteUser(input: Map<String, String>) : RemoteUser
}
