package uk.co.grahamcox.muck.service.user.dao

import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity

/**
 * Indication of the third-party Login Provider used to authenticate a user
 * @property id The ID of the Login Provider
 */
@NodeEntity(label = "LOGIN_PROVIDER")
data class LoginProviderModel(
        @Id var id: String
)
