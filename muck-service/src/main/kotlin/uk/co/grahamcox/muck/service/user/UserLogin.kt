package uk.co.grahamcox.muck.service.user

/**
 * Representation of a single login at a provider
 * @property provider The name of the provider
 * @property providerId The ID of the user at the provider
 * @property displayName The display name of the user at the provider
 */
data class UserLogin(
        val provider: String,
        val providerId: String,
        val displayName: String
)
