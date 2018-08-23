package uk.co.grahamcox.muck.service.authentication

import uk.co.grahamcox.muck.service.model.Id

/**
 * The ID of an Access Token
 */
data class AccessTokenId(override val id: String) : Id<String>
