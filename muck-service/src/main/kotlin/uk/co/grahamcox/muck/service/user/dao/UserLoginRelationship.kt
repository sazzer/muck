package uk.co.grahamcox.muck.service.user.dao

import org.neo4j.ogm.annotation.EndNode
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.RelationshipEntity
import org.neo4j.ogm.annotation.StartNode

/**
 * Relationship from a user to the login provider that authenticates it
 * @property providerId The ID of the user at this login provider
 * @property displayName The Display Name of the user at this login provider
 * @property user The user
 * @property loginProvider The login provider
 */
@RelationshipEntity(type = "LOGIN")
data class UserLoginRelationship(
        var providerId: String? = null,
        var displayName: String? = null
) {
    @Id
    var id: Long? = null

    @StartNode
    lateinit var user: UserModel

    @EndNode
    lateinit var loginProvider: LoginProviderModel
}
