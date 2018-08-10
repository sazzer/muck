package uk.co.grahamcox.muck.service.user

import java.util.*

/**
 * Representation of a hashed password
 * @property hash The Base64 Encoding of the Password Hash
 * @property salt The Base64 Encoding of the Password Salt
 */
data class Password(
        val hash: ByteArray,
        val salt: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Password

        if (!Arrays.equals(hash, other.hash)) return false
        if (!Arrays.equals(salt, other.salt)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(hash)
        result = 31 * result + Arrays.hashCode(salt)
        return result
    }
}
