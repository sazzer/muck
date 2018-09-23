package uk.co.grahamcox.muck.service.user

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * Data representing a user
 * @property email The email address of the user
 * @property displayName The display name of the user
 * @property logins The logins this user has at third party providers
 */
data class UserData(
        @field:NotBlank
        val email: String?,

        @field:NotBlank
        @field:NotNull
        val displayName: String,

        @field:Size(min = 1)
        @field:Valid
        val logins: Set<UserLogin>
)
