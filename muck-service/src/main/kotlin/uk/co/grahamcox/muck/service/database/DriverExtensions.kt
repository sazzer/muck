package uk.co.grahamcox.muck.service.database

import org.apache.commons.lang3.builder.ToStringBuilder
import org.neo4j.driver.v1.Driver
import org.neo4j.driver.v1.Session
import org.slf4j.LoggerFactory

/** The logger to use */
private val LOG = LoggerFactory.getLogger(Driver::class.java)

/**
 * Execute the given block in a new database session
 */
fun <T> Driver.exec(block: (Session) -> T) = this.session().use(block)

/**
 * Execute the given query in a new session
 */
fun Driver.query(query: String, params: Map<String, Any?> = mapOf()) = this.exec { session ->
    LOG.debug("Executing query: {} with params: {}", query, params)
    session.run(query, params)
}
