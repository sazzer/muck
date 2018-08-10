package uk.co.grahamcox.muck.service.user

/**
 * Representation of a hashed password
 * @property hash The Base64 Encoding of the Password Hash
 * @property salt The Base64 Encoding of the Password Salt
 */
data class Password(
        val hash: String,
        val salt: String
)
