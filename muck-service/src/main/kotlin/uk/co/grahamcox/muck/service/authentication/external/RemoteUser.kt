package uk.co.grahamcox.muck.service.authentication.external

/**
 * Representation of a user as described by the remote system
 */
data class RemoteUser(
        val providerId: String,
        val providerDisplayName: String,
        val email: String?,
        val displayName: String
)
