package uk.co.grahamcox.muck.service.user.dao

import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import uk.co.grahamcox.muck.service.database.ResourceNotFoundException
import uk.co.grahamcox.muck.service.model.Identity
import uk.co.grahamcox.muck.service.user.*

/**
 * Implementation of the User Service in terms of the Spring Data Repository
 */
@Transactional
class UserServiceImpl(private val userRepository: UserRepository) : UserRetriever {
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

        val userModel = userRepository.findById(id.id)
                .orElseThrow {
                    ResourceNotFoundException(id)
                }
        LOG.debug("Loaded database model {}", userModel)

        val result = UserResource(
                identity = Identity(
                        id = UserId(userModel.id!!),
                        version = userModel.version!!,
                        created = userModel.created!!,
                        updated = userModel.updated!!
                ),
                data = UserData(
                        email = userModel.email,
                        displayName = userModel.displayName!!,
                        logins = userModel.loginProviders.map { loginProvider ->
                            UserLogin(
                                    provider = loginProvider.loginProvider.id!!,
                                    providerId = loginProvider.providerId!!,
                                    displayName = loginProvider.displayName!!
                            )
                        }
                )
        )
        LOG.debug("Loaded user resource: {}", result)
        return result
    }
}
