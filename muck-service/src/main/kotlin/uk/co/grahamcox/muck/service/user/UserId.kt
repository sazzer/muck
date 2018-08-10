package uk.co.grahamcox.muck.service.user

import uk.co.grahamcox.muck.service.model.Id

/**
 * The ID of a User
 */
data class UserId(override val id: String) : Id
