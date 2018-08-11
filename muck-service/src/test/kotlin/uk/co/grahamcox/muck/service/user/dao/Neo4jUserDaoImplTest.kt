package uk.co.grahamcox.muck.service.user.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.neo4j.driver.v1.Driver
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.muck.service.database.ResourceNotFoundException
import uk.co.grahamcox.muck.service.database.query
import uk.co.grahamcox.muck.service.spring.SpringTestBase
import uk.co.grahamcox.muck.service.user.UserId
import java.time.Instant
import java.util.*

/**
 * Tests for the Neo4J User DAO
 */
internal class Neo4jUserDaoImplTest : SpringTestBase() {
    /** The User DAO to test */
    @Autowired
    private lateinit var userDao: UserDao

    /** The Neo4J Driver */
    @Autowired
    private lateinit var neo4j: Driver

    /**
     * Test getting an unknown user by ID
     */
    @Test
    fun testGetUnknownById() {
        val userId = UserId(UUID.randomUUID().toString())
        val e = Assertions.assertThrows(ResourceNotFoundException::class.java) {
            userDao.getById(userId)
        }

        Assertions.assertEquals(userId, e.id)
    }

    /**
     * Test getting a known user by ID
     */
    @Test
    fun testGetKnownById() {
        val userId = UserId(UUID.randomUUID().toString())
        neo4j.query("""CREATE (u:USER{
            id:{id},
            created:{created},
            updated: {updated},
            version: {version},
            display_name: {displayName},
            email: {email},
            password_hash: {hash},
            password_salt: {salt}
            })""", mapOf(
                "id" to userId.id,
                "created" to 1533985873000,
                "updated" to 1533985873000,
                "version" to "versionString",
                "displayName" to "Graham",
                "email" to "graham@example.com",
                "hash" to "aGFzaA==", // "hash"
                "salt" to "c2FsdA=="
        ))

        val user = userDao.getById(userId)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(userId, user.identity.id) },
                Executable { Assertions.assertEquals("versionString", user.identity.version) },
                Executable { Assertions.assertEquals(Instant.ofEpochMilli(1533985873000), user.identity.created) },
                Executable { Assertions.assertEquals(Instant.ofEpochMilli(1533985873000), user.identity.updated) },

                Executable { Assertions.assertEquals("Graham", user.data.displayName) },
                Executable { Assertions.assertEquals("graham@example.com", user.data.email) },
                Executable { Assertions.assertArrayEquals(Base64.getDecoder().decode("aGFzaA=="), user.data.password.hash) },
                Executable { Assertions.assertArrayEquals(Base64.getDecoder().decode("c2FsdA=="), user.data.password.salt) }
        )
    }
}
