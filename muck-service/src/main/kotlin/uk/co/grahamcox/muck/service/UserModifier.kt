package uk.co.grahamcox.muck.service

import uk.co.grahamcox.muck.service.user.UserData
import uk.co.grahamcox.muck.service.user.UserResource

/**
 * Mechanism to modify users in the database
 */
interface UserModifier {
    /**
     * Create a new user with the given data
     */
    fun create(user: UserData) : UserResource
}
