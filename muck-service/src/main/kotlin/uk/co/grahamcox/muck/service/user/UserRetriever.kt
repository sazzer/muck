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

    /**
     * Get the user with the given ID at the given Provider
     * @param provider The provider
     * @param providerId The ID of the user at the provider
     * @return the user, or null if it couldn't be found
     */
    fun getByProvider(provider: String, providerId: String) : UserResource?
}
