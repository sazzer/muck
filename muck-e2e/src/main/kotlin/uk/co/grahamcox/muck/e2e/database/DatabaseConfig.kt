package uk.co.grahamcox.muck.e2e.database

import org.neo4j.driver.v1.GraphDatabase
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

/**
 * Spring configuration for the Database access
 */
@Configuration
class DatabaseConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean {
                val address = env.getProperty("url.neo4j")
                GraphDatabase.driver(address)
            }
        }.initialize(context)
    }
}
