package uk.co.grahamcox.muck.service.user

/**
 * Mechanism to retrieve users from the database
 */
interface UserRetriever {
    /**
     * Get the user with the given ID
     * @param id The ID of the user
     * @return the user
     */
    fun getById(id: UserId) : UserResource
}
