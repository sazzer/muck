package uk.co.grahamcox.muck.service.user.password

import uk.co.grahamcox.muck.service.user.Password

/**
 * Mechanism to generate a cryptographic hash of a plaintext password
 */
interface PasswordHasher {
    /**
     * Generate a hash of a plaintext password
     * @param password The password to hash
     */
    fun hashPassword(password: String) : Password

    /**
     * Generate a hash of a plaintext password using the given salt
     * @param password The password to hash
     * @param salt The salt to use
     */
    fun hashPassword(password: String, salt: ByteArray) : Password
}
