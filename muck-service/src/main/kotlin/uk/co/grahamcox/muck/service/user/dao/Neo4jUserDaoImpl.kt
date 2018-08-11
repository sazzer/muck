package uk.co.grahamcox.muck.service.user.dao

import org.neo4j.driver.v1.Driver
import org.neo4j.driver.v1.exceptions.NoSuchRecordException
import org.neo4j.driver.v1.types.Node
import org.slf4j.LoggerFactory
import uk.co.grahamcox.muck.service.database.ResourceNotFoundException
import uk.co.grahamcox.muck.service.database.query
import uk.co.grahamcox.muck.service.model.Identity
import uk.co.grahamcox.muck.service.user.Password
import uk.co.grahamcox.muck.service.user.UserData
import uk.co.grahamcox.muck.service.user.UserId
import uk.co.grahamcox.muck.service.user.UserResource
import java.time.Clock
import java.time.Instant
import java.util.*

/**
 * Implementation of the User DAO in terms of Neo4J
 */
class Neo4jUserDaoImpl(
        private val driver: Driver,
        private val clock: Clock
) : UserDao {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(Neo4jUserDaoImpl::class.java)
    }
    /**
     * Get the User with the given ID
     * @param id The ID of the user
     * @return the user
     */
    override fun getById(id: UserId): UserResource {
        LOG.debug("Loading user with ID: {}", id)
        val userResult = driver.query("MATCH (u:USER {id:{id}}) RETURN u", mapOf("id" to id.id))

        return try {
            val userRecord = userResult.single()
            val userNode = userRecord.get("u").asNode()

            parseUserNode(userNode)
        } catch (e: NoSuchRecordException) {
            LOG.warn("No user found with ID: {}", id)
            throw ResourceNotFoundException(id)
        }
    }

    /**
     * Create a User with the given data
     * @param user The user data to create
     * @return the created user
     */
    override fun create(user: UserData): UserResource {
        val now = clock.instant()

        val userResult = driver.query("""CREATE (u:USER{
            id:{id},
            created:{created},
            updated: {updated},
            version: {version},
            display_name: {displayName},
            email: {email},
            password_hash: {hash},
            password_salt: {salt}
            }) RETURN u""", mapOf(
                "id" to UUID.randomUUID().toString(),
                "created" to now.toEpochMilli(),
                "updated" to now.toEpochMilli(),
                "version" to UUID.randomUUID().toString(),
                "displayName" to user.displayName,
                "email" to user.email,
                "hash" to user.password.hash, // "hash"
                "salt" to user.password.salt
        ))

        val userRecord = userResult.single()
        val userNode = userRecord.get("u").asNode()

        return parseUserNode(userNode)
    }

    /**
     * Parse a node representing a User
     * @param userNode The node to parse
     * @return the user
     */
    private fun parseUserNode(userNode: Node): UserResource {
        return UserResource(
                identity = Identity(
                        id = UserId(userNode.get("id").asString()),
                        version = userNode.get("version").asString(),
                        created = Instant.ofEpochMilli(userNode.get("created").asLong()),
                        updated = Instant.ofEpochMilli(userNode.get("updated").asLong())
                ),
                data = UserData(
                        email = userNode.get("email").asString(),
                        displayName = userNode.get("display_name").asString(),
                        password = Password(
                                hash = userNode.get("password_hash").asByteArray(),
                                salt = userNode.get("password_salt").asByteArray()
                        )
                )
        )
    }

}
