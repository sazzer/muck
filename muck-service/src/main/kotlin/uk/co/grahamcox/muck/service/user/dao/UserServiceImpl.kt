package uk.co.grahamcox.muck.service.user.dao

import org.neo4j.driver.v1.Record
import org.neo4j.driver.v1.exceptions.NoSuchRecordException
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import uk.co.grahamcox.muck.service.database.Neo4jOperations
import uk.co.grahamcox.muck.service.database.ResourceNotFoundException
import uk.co.grahamcox.muck.service.model.Identity
import uk.co.grahamcox.muck.service.user.*
import java.time.Clock
import java.time.Instant
import java.util.*

/**
 * Implementation of the User Service in terms of the Spring Data Repository
 */
@Transactional
class UserServiceImpl(
        private val neo4jOperations: Neo4jOperations,
        private val clock: Clock
) : UserService {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(UserServiceImpl::class.java)
    }

    /**
     * Get the user with the given ID
     * @param id The ID of the user
     * @return the user
     */
    @Transactional
    override fun getById(id: UserId): UserResource {
        val query = """
            MATCH
                (user:USER)
            WHERE
                user.id = {id}
            RETURN
                user,[ (user)-[login:LOGIN]->(provider:LOGIN_PROVIDER) | [ login, provider ] ] AS logins"""

        LOG.debug("Loading user with ID: {}", id)

        val result = try {
            neo4jOperations.queryOne(query,
                    mapOf("id" to id.id.toString()),
                    ::parseUserRecord)
        } catch (e: NoSuchRecordException) {
            throw ResourceNotFoundException(id)
        }
        LOG.debug("User record: {}", result)

        return result
    }

    /**
     * Get the user with the given ID at the given Provider
     * @param provider The provider
     * @param providerId The ID of the user at the provider
     * @return the user, or null if it couldn't be found
     */
    @Transactional
    override fun getByProvider(provider: String, providerId: String): UserResource? {
        val query = """
            MATCH
                (user:USER)-[l1:LOGIN]->(p1:LOGIN_PROVIDER)
            WHERE
                p1.id = {provider}
                AND l1.providerId = {providerId}
            RETURN
                user,[ (user)-[login:LOGIN]->(provider:LOGIN_PROVIDER) | [ login, provider ] ] AS logins"""

        LOG.debug("Loading user with ID {} at Provider {}", providerId, provider)

        val result = try {
            neo4jOperations.queryOne(query,
                    mapOf("provider" to provider, "providerId" to providerId),
                    ::parseUserRecord)
        } catch (e: NoSuchRecordException) {
            null
        }
        LOG.debug("User record: {}", result)

        return result
    }

    /**
     * Create a new user with the given data
     */
    @Transactional
    override fun create(user: UserData): UserResource {
        val identity = Identity(
                id = UserId(UUID.randomUUID()),
                version = UUID.randomUUID(),
                created = clock.instant(),
                updated = clock.instant()
        )

        neo4jOperations.execute("""
            CREATE (u:USER {
                id: {id},
                version: {version},
                created: {created},
                updated: {updated},
                email: {email},
                displayName: {displayName}
            })
        """, mapOf(
                "id" to identity.id.id.toString(),
                "version" to identity.version.toString(),
                "created" to identity.created.toString(),
                "updated" to identity.updated.toString(),
                "email" to user.email,
                "displayName" to user.displayName
        ))

        user.logins.forEach { login ->
            neo4jOperations.execute("MERGE (provider:LOGIN_PROVIDER {id: {provider}})", mapOf(
                    "provider" to login.provider
            ))
            neo4jOperations.execute("""
            MATCH
                (u:USER {id:{userId}}),
                (p:LOGIN_PROVIDER {id:{provider}})
            CREATE
                (u)-[:LOGIN {providerId:{providerId}, displayName:{displayName}}]->(p)
            """, mapOf(
                    "userId" to identity.id.id.toString(),
                    "provider" to login.provider,
                    "providerId" to login.providerId,
                    "displayName" to login.displayName
            ))
        }

        return UserResource(identity, user)
    }

    /**
     * Parse the given Record as a user result
     * @param record The record to parse
     * @return The user resource
     */
    private fun parseUserRecord(record: Record): UserResource {
        val user = record.get("user").asNode()
        val logins = record.get("logins").asList { loginData ->
            val login = loginData.get(0).asRelationship()
            val provider = loginData.get(1).asNode()

            UserLogin(
                    provider = provider.get("id").asString(),
                    providerId = login.get("providerId").asString(),
                    displayName = login.get("displayName").asString()
            )
        }.toSet()

        return UserResource(
                identity = Identity(
                        id = UserId(UUID.fromString(user.get("id").asString())),
                        version = UUID.fromString(user.get("version").asString()),
                        created = Instant.parse(user.get("created").asString()),
                        updated = Instant.parse(user.get("updated").asString())
                ),
                data = UserData(
                        email = user.get("email").asString(),
                        displayName = user.get("displayName").asString(),
                        logins = logins
                )
        )
    }
}
