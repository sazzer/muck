package uk.co.grahamcox.muck.service.user

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.function.Executable

/**
 * Unit tests for the Password class
 */
internal class PasswordTest {
    /**
     * Test the Equality methods - equals() and hashCode()
     */
    @TestFactory
    fun testEquals(): List<DynamicTest> {
        val base = buildPassword("hash", "salt")
        return listOf(
                buildPassword("hash", "salt"),
                base
        )
                .map {
                    DynamicTest.dynamicTest("$base == $it") {
                        Assertions.assertAll(
                                Executable { Assertions.assertEquals(base, it) },
                                Executable { Assertions.assertEquals(base.hashCode(), it.hashCode()) }
                        )

                    }
                }
    }

    /**
     * Test the Equality methods - equals() and hashCode()
     */
    @TestFactory
    fun testNotEquals(): List<DynamicTest> {
        val base = buildPassword("hash", "salt")
        return listOf(
                buildPassword("hash1", "salt"),
                buildPassword("Hash", "salt"),
                buildPassword("hash", "salt1"),
                buildPassword("hash", "Salt"),
                buildPassword("salt", "hash"),
                buildPassword("hash", "hash"),
                buildPassword("salt", "salt"),
                buildPassword("hash", ""),
                buildPassword("", "salt"),
                buildPassword("hash", "salt "),
                buildPassword("hash", " salt"),
                buildPassword("hash ", "salt"),
                buildPassword(" hash", ""),
                null
        )
                .map {
                    DynamicTest.dynamicTest("$base != $it") {
                        Assertions.assertAll(
                                Executable { Assertions.assertNotEquals(base, it) },
                                Executable { Assertions.assertNotEquals(base.hashCode(), it?.hashCode()) }
                        )

                    }
                }
    }

    /**
     * Helper to build a Password object
     */
    private fun buildPassword(hash: String, salt: String) =
            Password(hash.toByteArray(), salt.toByteArray())
}
