package uk.co.grahamcox.muck.service.user.dao

import org.springframework.data.neo4j.repository.Neo4jRepository
import java.util.*

/**
 * Repository of User records
 */
interface UserRepository : Neo4jRepository<UserModel, UUID>
