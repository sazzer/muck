package uk.co.grahamcox.muck.service.user.dao

import org.neo4j.driver.v1.Driver
import org.neo4j.driver.v1.exceptions.NoSuchRecordException
import org.slf4j.LoggerFactory
import uk.co.grahamcox.muck.service.database.ResourceNotFoundException
import uk.co.grahamcox.muck.service.database.query
import uk.co.grahamcox.muck.service.model.Identity
import uk.co.grahamcox.muck.service.user.Password
import uk.co.grahamcox.muck.service.user.UserData
import uk.co.grahamcox.muck.service.user.UserId
import uk.co.grahamcox.muck.service.user.UserResource
import java.time.Instant
import java.util.*

/**
 * Implementation of the User DAO in terms of Neo4J
 */
class Neo4jUserDaoImpl(
        private val driver: Driver
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

            val base64Decoder = Base64.getDecoder()
            UserResource(
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
                                    hash = base64Decoder.decode(userNode.get("password_hash").asString()),
                                    salt = base64Decoder.decode(userNode.get("password_salt").asString())
                            )
                    )
            )

        } catch (e: NoSuchRecordException) {
            LOG.warn("No user found with ID: {}", id)
            throw ResourceNotFoundException(id)
        }
    }
}
