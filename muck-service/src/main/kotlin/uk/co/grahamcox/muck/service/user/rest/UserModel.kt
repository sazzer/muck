package uk.co.grahamcox.muck.service.user.rest

import com.fasterxml.jackson.annotation.JsonProperty
import uk.co.grahamcox.muck.service.rest.hal.Link

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
        val email: String?,
        val displayName: String,
        val logins: List<UserLoginModel>

)
