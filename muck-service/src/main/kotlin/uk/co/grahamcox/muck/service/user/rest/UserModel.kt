package uk.co.grahamcox.muck.service.user.rest

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import uk.co.grahamcox.muck.service.rest.hal.Link
import java.util.*

/**
 * The HAL links for a User
 */
data class UserLinks(
        val self: Link
)

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
        @JsonProperty("_links") val links: UserLinks,
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
