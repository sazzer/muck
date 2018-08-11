package uk.co.grahamcox.muck.service.user.dao

import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity

/**
 * Representation of a User Model in the Neo4J Database
 */
@NodeEntity
data class UserModel(
        @Id @GeneratedValue val id: Long? = null,

        val email: String,

        val displayName: String
)
