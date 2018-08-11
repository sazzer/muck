package uk.co.grahamcox.muck.service.user.dao

import uk.co.grahamcox.muck.service.user.UserData
import uk.co.grahamcox.muck.service.user.UserId
import uk.co.grahamcox.muck.service.user.UserResource

/**
 * DAO for working with Users
 */
interface UserDao {
    /**
     * Get the User with the given ID
     * @param id The ID of the user
     * @return the user
     */
    fun getById(id: UserId): UserResource

    /**
     * Create a User with the given data
     * @param user The user data to create
     * @return the created user
     */
    fun create(user: UserData): UserResource
}
