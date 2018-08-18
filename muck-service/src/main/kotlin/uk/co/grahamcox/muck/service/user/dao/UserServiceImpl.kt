package uk.co.grahamcox.muck.service.user.dao

import uk.co.grahamcox.muck.service.database.ResourceNotFoundException
import uk.co.grahamcox.muck.service.user.UserId
import uk.co.grahamcox.muck.service.user.UserResource
import uk.co.grahamcox.muck.service.user.UserRetriever

/**
 * Implementation of the User Service in terms of the Spring Data Repository
 */
class UserServiceImpl(private val userRepository: UserRepository) : UserRetriever {
    /**
     * Get the user with the given ID
     * @param id The ID of the user
     * @return the user
     */
    override fun getById(id: UserId): UserResource {
        val userModel = userRepository.findById(id.id)
                .orElseThrow {
                    ResourceNotFoundException(id)
                }
        TODO(userModel.toString())
    }
}
