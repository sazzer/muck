package uk.co.grahamcox.muck.service.user

import uk.co.grahamcox.muck.service.model.Identity
import javax.validation.ConstraintViolationException
import javax.validation.Validator

/**
 * User Service implementation that performs validation on the incoming objects first
 */
class ValidatingUserService(
        private val delegate: UserService,
        private val validator: Validator
) : UserService {
    /**
     * Get the user with the given ID
     * @param id The ID of the user
     * @return the user
     */
    override fun getById(id: UserId): UserResource {
        return delegate.getById(id)
    }

    /**
     * Get the user with the given ID at the given Provider
     * @param provider The provider
     * @param providerId The ID of the user at the provider
     * @return the user, or null if it couldn't be found
     */
    override fun getByProvider(provider: String, providerId: String): UserResource? {
        return delegate.getByProvider(provider, providerId)
    }

    /**
     * Create a new user with the given data
     */
    override fun create(user: UserData): UserResource {
        val violations = validator.validate(user)
        if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        }
        return delegate.create(user)
    }

    /**
     * Update a user with the given data
     */
    override fun update(oldIdentity: Identity<UserId>, user: UserData): UserResource {
        val violations = validator.validate(user)
        if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        }
        return delegate.update(oldIdentity, user)
    }
}
