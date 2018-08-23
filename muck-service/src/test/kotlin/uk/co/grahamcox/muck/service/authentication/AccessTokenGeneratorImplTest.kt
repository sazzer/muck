package uk.co.grahamcox.muck.service.authentication

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import uk.co.grahamcox.muck.service.user.UserId
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.util.*

/**
 * Unit tests for the Access Token Generator
 */
internal class AccessTokenGeneratorImplTest {
    /** the "current time */
    private val NOW = Instant.parse("2018-08-23T07:45:00Z")

    /** The access token duration */
    private val DURATION = Duration.ofDays(2)

    /** The Test subject */
    private val testSubject = AccessTokenGeneratorImpl(
            clock = Clock.fixed(NOW, ZoneId.of("UTC")),
            duration = DURATION
    )

    /**
     * Test generating an access token
     */
    @Test
    fun test() {
        val userId = UserId(UUID.randomUUID())

        val accessToken = testSubject.generate(userId)
        Assertions.assertAll(
                Executable { Assertions.assertEquals(userId, accessToken.user) },
                Executable { Assertions.assertEquals(NOW, accessToken.created) },
                Executable { Assertions.assertEquals(NOW.plus(DURATION), accessToken.expires) }
        )
    }
}
