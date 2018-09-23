package uk.co.grahamcox.muck.service.user.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.muck.service.database.Neo4jOperations
import uk.co.grahamcox.muck.service.database.OptimisticLockException
import uk.co.grahamcox.muck.service.database.ResourceNotFoundException
import uk.co.grahamcox.muck.service.model.Identity
import uk.co.grahamcox.muck.service.spring.SpringTestBase
import uk.co.grahamcox.muck.service.user.UserData
import uk.co.grahamcox.muck.service.user.UserId
import uk.co.grahamcox.muck.service.user.UserLogin
import java.time.Clock
import java.time.Instant
import java.util.*

/**
 * Integration test for the UserService
 */
internal class UserServiceImplIT : SpringTestBase() {
    /** The database connection */
    @Autowired
    private lateinit var neo4jOperations: Neo4jOperations

    /** The clock */
    @Autowired
    private lateinit var clock: Clock

    /** The test subject */
    private lateinit var userServiceImpl: UserServiceImpl

    /**
     * Set up the test subject
     */
    @BeforeEach
    fun setup() {
        userServiceImpl = UserServiceImpl(neo4jOperations, clock)
    }

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
     * Test getting a known user with no login providers
     */
    @Test
    fun testGetKnownById() {
        val userId = UserId(UUID.randomUUID())
        val version = UUID.randomUUID()
        val now = Instant.now()
        createUser(userId, version, now, false)

        val user = userServiceImpl.getById(userId)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(userId, user.identity.id) },
                Executable { Assertions.assertEquals(version, user.identity.version) },
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
        val version = UUID.randomUUID()
        val now = Instant.now()
        createUser(userId, version, now, true)

        val user = userServiceImpl.getById(userId)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(userId, user.identity.id) },
                Executable { Assertions.assertEquals(version, user.identity.version) },
                Executable { Assertions.assertEquals(now, user.identity.created) },
                Executable { Assertions.assertEquals(now, user.identity.updated) },

                Executable { Assertions.assertEquals("graham@example.com", user.data.email) },
                Executable { Assertions.assertEquals("Graham", user.data.displayName) },
                Executable { Assertions.assertEquals(2, user.data.logins.size) },

                Executable { Assertions.assertTrue(user.data.logins.contains(UserLogin("google", "123", "graham@example.com"))) },
                Executable { Assertions.assertTrue(user.data.logins.contains(UserLogin("twitter", "987", "@grahamexample"))) }
        )
    }

    /**
     * Test getting an unknown user by Provider ID
     */
    @Test
    fun testGetUnknownByProviderId() {
        val user = userServiceImpl.getByProvider("google", "123")

        Assertions.assertNull(user)
    }

    /**
     * Test getting a known user by login provider
     */
    @Test
    fun testGetKnownByProvider() {
        val userId = UserId(UUID.randomUUID())
        val version = UUID.randomUUID()
        val now = Instant.now()
        createUser(userId, version, now, true)

        val user = userServiceImpl.getByProvider("google", "123")

        Assertions.assertNotNull(user)

        user!!

        Assertions.assertAll(
                Executable { Assertions.assertEquals(userId, user.identity.id) },
                Executable { Assertions.assertEquals(version, user.identity.version) },
                Executable { Assertions.assertEquals(now, user.identity.created) },
                Executable { Assertions.assertEquals(now, user.identity.updated) },

                Executable { Assertions.assertEquals("graham@example.com", user.data.email) },
                Executable { Assertions.assertEquals("Graham", user.data.displayName) },
                Executable { Assertions.assertEquals(2, user.data.logins.size) },

                Executable { Assertions.assertTrue(user.data.logins.contains(UserLogin("google", "123", "graham@example.com"))) },
                Executable { Assertions.assertTrue(user.data.logins.contains(UserLogin("twitter", "987", "@grahamexample"))) }
        )
    }

    /**
     * Test creating a user
     */
    @Test
    fun testCreateSimpleUser() {
        val created = userServiceImpl.create(UserData(
                email = "graham@example.com",
                displayName = "Graham",
                logins = emptySet()
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals("graham@example.com", created.data.email) },
                Executable { Assertions.assertEquals("Graham", created.data.displayName) },
                Executable { Assertions.assertEquals(0, created.data.logins.size) }
        )

        val loaded = userServiceImpl.getById(created.identity.id)

        Assertions.assertEquals(created, loaded)
    }

    /**
     * Test creating a user that has logins at third party providers, where the provider nodes don't exist
     */
    @Test
    fun testCreateUserWithLogins() {
        val created = userServiceImpl.create(UserData(
                email = "graham@example.com",
                displayName = "Graham",
                logins = setOf(
                        UserLogin("google", "123", "graham@example.com"),
                        UserLogin("twitter", "987", "@grahamexample")
                )
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals("graham@example.com", created.data.email) },
                Executable { Assertions.assertEquals("Graham", created.data.displayName) },
                Executable { Assertions.assertEquals(2, created.data.logins.size) },

                Executable { Assertions.assertTrue(created.data.logins.contains(UserLogin("google", "123", "graham@example.com"))) },
                Executable { Assertions.assertTrue(created.data.logins.contains(UserLogin("twitter", "987", "@grahamexample"))) }
        )

        val loaded = userServiceImpl.getById(created.identity.id)

        Assertions.assertEquals(created, loaded)
    }

    /**
     * Test creating a user that has logins at third party providers, where the provider nodes already exist
     */
    @Test
    fun testCreateUserWithLoginsExistingProviders() {
        execute("""CREATE (p:LOGIN_PROVIDER {id:"google"})""")
        execute("""CREATE (p:LOGIN_PROVIDER {id:"twitter"})""")

        val created = userServiceImpl.create(UserData(
                email = "graham@example.com",
                displayName = "Graham",
                logins = setOf(
                        UserLogin("google", "123", "graham@example.com"),
                        UserLogin("twitter", "987", "@grahamexample")
                )
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals("graham@example.com", created.data.email) },
                Executable { Assertions.assertEquals("Graham", created.data.displayName) },
                Executable { Assertions.assertEquals(2, created.data.logins.size) },

                Executable { Assertions.assertTrue(created.data.logins.contains(UserLogin("google", "123", "graham@example.com"))) },
                Executable { Assertions.assertTrue(created.data.logins.contains(UserLogin("twitter", "987", "@grahamexample"))) }
        )

        val loaded = userServiceImpl.getById(created.identity.id)

        Assertions.assertEquals(created, loaded)
    }

    /**
     * Test updating the user data for a user
     */
    @Test
    fun testUpdateUserData() {
        val userId = UserId(UUID.randomUUID())
        createUser(userId, UUID.randomUUID(), Instant.now().minusSeconds(30), true)

        val user = userServiceImpl.getById(userId)

        val updated = userServiceImpl.update(user.identity,
                UserData(
                        email = "newEmail@other.domain",
                        displayName = "New User",
                        logins = setOf(
                                UserLogin(
                                        provider = "facebook",
                                        providerId = "1234567",
                                        displayName = "New User"
                                )
                        )
                ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(user.identity.id, updated.identity.id) },
                Executable { Assertions.assertEquals(user.identity.created, updated.identity.created) },
                Executable { Assertions.assertNotEquals(user.identity.version, updated.identity.version) },
                Executable { Assertions.assertNotEquals(user.identity.updated, updated.identity.updated) },

                Executable { Assertions.assertEquals("newEmail@other.domain", updated.data.email) },
                Executable { Assertions.assertEquals("New User", updated.data.displayName) },
                Executable { Assertions.assertEquals(1, updated.data.logins.size) },

                Executable { Assertions.assertTrue(updated.data.logins.contains(UserLogin("facebook", "1234567", "New User"))) }
        )

        val loaded = userServiceImpl.getById(updated.identity.id)

        Assertions.assertEquals(updated, loaded)
    }

    /**
     * Test updating the user data for a user that doesn't exist
     */
    @Test
    fun testUpdateUnknownUser() {
        val userId = UserId(UUID.randomUUID())

        val e = Assertions.assertThrows(ResourceNotFoundException::class.java) {
            userServiceImpl.update(Identity(
                    id = userId,
                    version = UUID.randomUUID(),
                    created = Instant.now(),
                    updated = Instant.now()),
                    UserData(
                            email = "newEmail@other.domain",
                            displayName = "New User",
                            logins = setOf(
                                    UserLogin(
                                            provider = "facebook",
                                            providerId = "1234567",
                                            displayName = "New User"
                                    )
                            )
                    ))
        }

        Assertions.assertEquals(userId, e.id)
    }


    /**
     * Test updating the user data for a user that exists but has a different version
     */
    @Test
    fun testUpdateUserWrongVersion() {
        val userId = UserId(UUID.randomUUID())
        createUser(userId, UUID.randomUUID(), Instant.now().minusSeconds(30), true)

        val e = Assertions.assertThrows(OptimisticLockException::class.java) {
            userServiceImpl.update(Identity(
                    id = userId,
                    version = UUID.randomUUID(),
                    created = Instant.now(),
                    updated = Instant.now()),
                    UserData(
                            email = "newEmail@other.domain",
                            displayName = "New User",
                            logins = setOf(
                                    UserLogin(
                                            provider = "facebook",
                                            providerId = "1234567",
                                            displayName = "New User"
                                    )
                            )
                    ))
        }

        Assertions.assertEquals(userId, e.id)
    }

    /**
     * Create the user record to use in the tests
     */
    private fun createUser(userId: UserId, version: UUID, now: Instant, withLogins: Boolean) {
        execute("""CREATE (p:LOGIN_PROVIDER {id:"google"})""")
        execute("""CREATE (p:LOGIN_PROVIDER {id:"twitter"})""")
        execute("CREATE (u:USER {id:{id}, version:{version}, created:{now}, updated:{now}, email:{email}, displayName:{displayName}})",
                mapOf(
                        "id" to userId.id.toString(),
                        "version" to version.toString(),
                        "now" to now.toString(),
                        "email" to "graham@example.com",
                        "displayName" to "Graham"
                ))
        if (withLogins) {
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
        }
    }
}
