package uk.co.grahamcox.muck.service.user.rest

import java.util.*


/**
 * The representation of a login with a third-party provider
 */
data class UserLoginModel(
        val provider: String,
        val providerId: String,
        val displayName: String
)

/**
 * The representation of a user
 */
data class UserModel(
        val id: UUID,
        val email: String?,
        val displayName: String,
        val logins: List<UserLoginModel>
)

/**
 * The representation of a user for input purposes
 */
data class UserInputModel(
        val email: String?,
        val displayName: String,
        val logins: List<UserLoginModel>
)
