package uk.co.grahamcox.muck.service.user

import uk.co.grahamcox.muck.service.UserModifier

/**
 * Mechanism for both reading and modifying users
 */
interface UserService : UserRetriever, UserModifier
