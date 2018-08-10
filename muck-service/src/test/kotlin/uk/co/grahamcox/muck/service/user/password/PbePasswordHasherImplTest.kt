package uk.co.grahamcox.muck.service.user.password

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.security.NoSuchAlgorithmException

/**
 * Unit tests for the PBE Password Hasher
 */
internal class PbePasswordHasherImplTest {
    /**
     * Test when the hasher name is invalid
     */
    @Test
    fun testInvalidHasherName() {
        Assertions.assertThrows(NoSuchAlgorithmException::class.java) {
            PbePasswordHasherImpl("Unknown", 1, 1, 1)
        }
    }

    /**
     * Test when the salt size is invalid
     */
    @Test
    fun testInvalidSaltSize() {
        Assertions.assertThrows(NegativeArraySizeException::class.java) {
            PbePasswordHasherImpl("PBKDF2WithHmacSHA512", -1, 1, 1)
        }
    }

    /**
     * Test when the iterations value is invalid
     */
    @Test
    fun testInvalidIterations() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            PbePasswordHasherImpl("PBKDF2WithHmacSHA512", 256, -1, 1)
        }
    }

    /**
     * Test when the key length value is invalid
     */
    @Test
    fun testInvalidKeyLength() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            PbePasswordHasherImpl("PBKDF2WithHmacSHA512", 256, 10000, -1)
        }
    }
    /**
     * Test generating hashes for the same password twice generates different hashes
     */
    @Test
    fun testGenerateRepeat() {
        val testSubject = PbePasswordHasherImpl("PBKDF2WithHmacSHA512", 256, 10000, 256)

        val password1 = testSubject.hashPassword("password")
        val password2 = testSubject.hashPassword("password")

        Assertions.assertNotEquals(password1, password2)
    }

    /**
     * Test generating hashes for the same password twice with the same salt generates the same hash
     */
    @Test
    fun testGenerateRepeatSameSalt() {
        val testSubject = PbePasswordHasherImpl("PBKDF2WithHmacSHA512", 256, 10000, 256)

        val password1 = testSubject.hashPassword("password")
        val password2 = testSubject.hashPassword("password", password1.salt)

        Assertions.assertEquals(password1, password2)
    }
}
