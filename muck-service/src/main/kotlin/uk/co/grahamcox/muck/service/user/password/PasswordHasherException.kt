package uk.co.grahamcox.muck.service.user.password

/**
 * Exception to indicate that hashing a password failed
 */
class PasswordHasherException(message: String, cause: Exception) : RuntimeException(message, cause)
