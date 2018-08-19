package uk.co.grahamcox.muck.service.user.dao

import org.slf4j.LoggerFactory
import uk.co.grahamcox.muck.service.user.UserId
import uk.co.grahamcox.muck.service.user.UserResource
import uk.co.grahamcox.muck.service.user.UserRetriever

/**
 * Implementation of the User Service in terms of the Spring Data Repository
 */
class UserServiceImpl() : UserRetriever {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(UserServiceImpl::class.java)
    }

    /**
     * Get the user with the given ID
     * @param id The ID of the user
     * @return the user
     */
    override fun getById(id: UserId): UserResource {
        LOG.debug("Loading user with ID: {}", id)

        TODO()
    }
}
