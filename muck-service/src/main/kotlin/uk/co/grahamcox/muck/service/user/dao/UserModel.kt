package uk.co.grahamcox.muck.service.user.dao

import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Version
import java.time.Instant
import java.util.*

/**
 * Representation of a User Model in the Neo4J Database
 */
@NodeEntity(label = "USER")
data class UserModel(
        @Id var id: UUID? = null,

        @Version var version: Long? = null,

        var created: Instant,

        var updated: Instant,

        var email: String,

        var displayName: String
)
