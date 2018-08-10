package uk.co.grahamcox.muck.service.user

/**
 * Data representing a user
 * @property email The email address of the user
 * @property displayName The display name of the user
 * @property password The password of the user
 */
data class UserData(
        val email: String,
        val displayName: String,
        val password: Password
)
