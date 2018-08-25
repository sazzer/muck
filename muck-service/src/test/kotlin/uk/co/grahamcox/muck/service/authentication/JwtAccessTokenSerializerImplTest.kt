package uk.co.grahamcox.muck.service.authentication

import com.auth0.jwt.algorithms.Algorithm
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.co.grahamcox.muck.service.user.UserId
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Unit tests for [JwtAccessTokenSerializerImpl]
 */
internal class JwtAccessTokenSerializerImplTest {
    /** The test subject */
    private val testSubject = JwtAccessTokenSerializerImpl(Algorithm.HMAC512("superSecret"))

    /**
     * Test serializing an Access Token into a String
     */
    @Test
    fun testSerialize() {
        val accessToken = AccessToken(
                id = AccessTokenId("accessTokenId"),
                user = UserId(UUID.fromString("00000000-0000-0000-0000-000000000000")),
                created = Instant.parse("2018-06-03T12:09:00Z"),
                expires = Instant.parse("2019-06-03T12:09:00Z")
        )
        val jwt = testSubject.serialize(accessToken)

        Assertions.assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJhdWQiOiJ1ay5jby5ncmFoYW1jb3gubXVjay5zZXJ2aWNlLmF1dGhlbnRpY2F0aW9uLkp3dEFjY2Vzc1Rva2VuU2VyaWFsaXplckltcGwiLCJzdWIiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJpc3MiOiJ1ay5jby5ncmFoYW1jb3gubXVjay5zZXJ2aWNlLmF1dGhlbnRpY2F0aW9uLkp3dEFjY2Vzc1Rva2VuU2VyaWFsaXplckltcGwiLCJleHAiOjE1NTk1NjM3NDAsImlhdCI6MTUyODAyNzc0MCwianRpIjoiYWNjZXNzVG9rZW5JZCJ9.wVb3zGsovwlZkiKSzfKCtNwMfFhyL7cUZglLqtiTWUAvjreZZxE4bUiGlCMd2xyxGlJgqAyUV6aomEjIuYeBmQ",
                jwt)
    }

    /**
     * Test serializing an Access Token into a String and then back again
     * Unfortunately the JWT Library automatically validates the Expiry Time against the system clock, so we can't use
     * a pre-defined string here
     */
    @Test
    fun testSerializeAndDeserialize() {
        val now = Instant.now().truncatedTo(ChronoUnit.SECONDS)
        val accessToken = AccessToken(
                id = AccessTokenId("accessTokenId"),
                user = UserId(UUID.fromString("00000000-0000-0000-0000-000000000000")),
                created = now,
                expires = now.plus(Duration.ofDays(1))
        )

        val jwt = testSubject.serialize(accessToken)
        val result = testSubject.deserialize(jwt)

        Assertions.assertEquals(accessToken, result)
    }

    /**
     * Test deserializing an access token that has expired
     */
    @Test
    fun testDeserializeExpired() {
        val now = Instant.now().truncatedTo(ChronoUnit.SECONDS)
        val accessToken = AccessToken(
                id = AccessTokenId("accessTokenId"),
                user = UserId(UUID.fromString("00000000-0000-0000-0000-000000000000")),
                created = now,
                expires = now.minus(Duration.ofDays(1))
        )

        val jwt = testSubject.serialize(accessToken)
        Assertions.assertThrows(InvalidAccessTokenException::class.java) {
            testSubject.deserialize(jwt)
        }
    }

    /**
     * Test deserializing an access token that is just not valid
     */
    @Test
    fun testDeserializeInvalid() {
        Assertions.assertThrows(InvalidAccessTokenException::class.java) {
            testSubject.deserialize("invalid")
        }
    }

    /**
     * Test deserializing an access token that was signed with a different key
     */
    @Test
    fun testDeserializeBadSignature() {
        val now = Instant.now().truncatedTo(ChronoUnit.SECONDS)
        val accessToken = AccessToken(
                id = AccessTokenId("accessTokenId"),
                user = UserId(UUID.fromString("00000000-0000-0000-0000-000000000000")),
                created = now,
                expires = now.plus(Duration.ofDays(1))
        )

        val jwt = JwtAccessTokenSerializerImpl(Algorithm.HMAC512("otherKey"))
                .serialize(accessToken)

        Assertions.assertThrows(InvalidAccessTokenException::class.java) {
            testSubject.deserialize(jwt)
        }
    }
}
