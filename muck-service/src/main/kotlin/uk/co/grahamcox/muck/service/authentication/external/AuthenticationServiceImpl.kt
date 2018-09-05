package uk.co.grahamcox.muck.service.authentication.external

import org.slf4j.LoggerFactory
import uk.co.grahamcox.muck.service.user.UserData
import uk.co.grahamcox.muck.service.user.UserLogin
import uk.co.grahamcox.muck.service.user.UserResource
import uk.co.grahamcox.muck.service.user.UserService
import java.net.URI

/**
 * Standard strategy-based implementation of the Authentication Service
 */
class AuthenticationServiceImpl(
        override val id: String,
        private val redirectBuilder: AuthenticationRedirectBuilder,
        private val remoteUserLoader: RemoteUserLoader,
        private val userService: UserService
) : AuthenticationService {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(AuthenticationServiceImpl::class.java)
    }

    /**
     * Build the URI to redirect the user to
     * @return the URI to redirect the user to
     */
    override fun buildRedirectUri(): URI {
        return redirectBuilder.build()
    }

    /**
     * Complete user authentication and return the user that has authenticated
     */
    override fun completeAuthentication(input: Map<String, String>): UserResource {
        val remoteUser = remoteUserLoader.loadRemoteUser(input)

        val existingUser = userService.getByProvider(id, remoteUser.providerId)

        return if (existingUser == null) {
            val newUser = userService.create(UserData(
                    email = remoteUser.email,
                    displayName = remoteUser.displayName,
                    logins = setOf(
                            UserLogin(
                                    provider = id,
                                    providerId = remoteUser.providerId,
                                    displayName = remoteUser.providerDisplayName
                            )
                    )
            ))
            LOG.debug("Authenticated user doesn't exist. Creating new one: {}", newUser)
            newUser
        } else {
            LOG.debug("Authenticated user already exists: {}", existingUser)
            existingUser
        }
    }
}
