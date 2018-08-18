package uk.co.grahamcox.muck.service.user.dao

import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.Version
import java.time.Instant
import java.util.*

/**
 * Representation of a User Model in the Neo4J Database
 * @property id The ID of the User
 * @property version The current version of the user record
 * @property created When the user was created
 * @property updated When the user was last updated
 * @property email The email address of the user
 * @property displayName The display name of the user
 * @property loginProviders The login details of the user at third party providers
 */
@NodeEntity(label = "USER")
data class UserModel(
        @Id
        var id: UUID? = null,

        @Version
        var version: Long? = null,

        var created: Instant,
        var updated: Instant,
        var email: String?,
        var displayName: String,

        @Relationship(type = "LOGIN")
        var loginProviders: Collection<UserLoginRelationship>
)
