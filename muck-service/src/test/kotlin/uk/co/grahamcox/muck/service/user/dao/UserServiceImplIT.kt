package uk.co.grahamcox.muck.service.user.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.muck.service.database.ResourceNotFoundException
import uk.co.grahamcox.muck.service.spring.SpringTestBase
import uk.co.grahamcox.muck.service.user.UserId
import uk.co.grahamcox.muck.service.user.UserLogin
import java.time.Instant
import java.util.*

/**
 * Integration test for the UserService
 */
@Disabled
internal class UserServiceImplIT : SpringTestBase() {
    /** The test subject */
    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl

    /**
     * Test getting an unknown user by ID
     */
    @Test
    fun testGetUnknownById() {
        val userId = UserId(UUID.randomUUID())

        val e = Assertions.assertThrows(ResourceNotFoundException::class.java) {
            userServiceImpl.getById(userId)
        }

        Assertions.assertEquals(userId, e.id)
    }

    /**
     * Test gettign a known user with no login providers
     */
    @Test
    fun testGetKnownById() {
        val userId = UserId(UUID.randomUUID())
        val now = Instant.now()
        execute("CREATE (u:USER {id:{id}, version:0, created:{now}, updated:{now}, email:{email}, displayName:{displayName}})",
                mapOf(
                        "id" to userId.id.toString(),
                        "now" to now.toString(),
                        "email" to "graham@example.com",
                        "displayName" to "Graham"
                ))

        val user = userServiceImpl.getById(userId)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(userId, user.identity.id) },
                Executable { Assertions.assertEquals(0, user.identity.version) },
                Executable { Assertions.assertEquals(now, user.identity.created) },
                Executable { Assertions.assertEquals(now, user.identity.updated) },

                Executable { Assertions.assertEquals("graham@example.com", user.data.email) },
                Executable { Assertions.assertEquals("Graham", user.data.displayName) },
                Executable { Assertions.assertEquals(0, user.data.logins.size) }
        )
    }

    /**
     * Test getting a known user with login providers
     */
    @Test
    fun testGetKnownByIdWithProviders() {
        val userId = UserId(UUID.randomUUID())
        val now = Instant.now()
        execute("""CREATE (p:LOGIN_PROVIDER {id:"google"})""")
        execute("""CREATE (p:LOGIN_PROVIDER {id:"twitter"})""")
        execute("CREATE (u:USER {id:{id}, version:0, created:{now}, updated:{now}, email:{email}, displayName:{displayName}})",
                mapOf(
                        "id" to userId.id.toString(),
                        "now" to now.toString(),
                        "email" to "graham@example.com",
                        "displayName" to "Graham"
                ))
        execute("""MATCH (u:USER {id:{userId}}), (p:LOGIN_PROVIDER {id:"google"}) CREATE (u)-[:LOGIN {providerId:{providerId}, displayName:{displayName}}]->(p)""",
                mapOf(
                        "userId" to userId.id.toString(),
                        "providerId" to "123",
                        "displayName" to "graham@example.com"
                ))
        execute("""MATCH (u:USER {id:{userId}}), (p:LOGIN_PROVIDER {id:"twitter"}) CREATE (u)-[:LOGIN {providerId:{providerId}, displayName:{displayName}}]->(p)""",
                mapOf(
                        "userId" to userId.id.toString(),
                        "providerId" to "987",
                        "displayName" to "@grahamexample"
                ))

        val user = userServiceImpl.getById(userId)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(userId, user.identity.id) },
                Executable { Assertions.assertEquals(0, user.identity.version) },
                Executable { Assertions.assertEquals(now, user.identity.created) },
                Executable { Assertions.assertEquals(now, user.identity.updated) },

                Executable { Assertions.assertEquals("graham@example.com", user.data.email) },
                Executable { Assertions.assertEquals("Graham", user.data.displayName) },
                Executable { Assertions.assertEquals(2, user.data.logins.size) },

                Executable { Assertions.assertTrue(user.data.logins.contains(UserLogin("google", "123", "graham@example.com"))) },
                Executable { Assertions.assertTrue(user.data.logins.contains(UserLogin("twitter", "987", "@grahamexample"))) }
        )
    }
}
