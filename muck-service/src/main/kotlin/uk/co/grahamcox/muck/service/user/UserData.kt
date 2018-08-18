package uk.co.grahamcox.muck.service.user

/**
 * Data representing a user
 * @property email The email address of the user
 * @property displayName The display name of the user
 * @property logins The logins this user has at third party providers
 */
data class UserData(
        val email: String,
        val displayName: String,
        val logins: Collection<UserLogin>
)
