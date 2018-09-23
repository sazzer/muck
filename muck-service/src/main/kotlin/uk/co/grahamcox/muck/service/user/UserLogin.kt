package uk.co.grahamcox.muck.service.user

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * Representation of a single login at a provider
 * @property provider The name of the provider
 * @property providerId The ID of the user at the provider
 * @property displayName The display name of the user at the provider
 */
data class UserLogin(
        @field:NotBlank
        @field:NotNull
        val provider: String,

        @field:NotBlank
        @field:NotNull
        val providerId: String,

        @field:NotBlank
        @field:NotNull
        val displayName: String
)
