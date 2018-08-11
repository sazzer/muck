package uk.co.grahamcox.muck.service.database

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories

/**
 * Spring configuration for the database
 */
@Configuration
@EnableNeo4jRepositories(basePackages = ["uk.co.grahamcox.muck.service"])
class DatabaseConfig {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(DatabaseConfig::class.java)
    }
}
