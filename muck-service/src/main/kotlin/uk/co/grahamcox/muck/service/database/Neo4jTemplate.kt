package uk.co.grahamcox.muck.service.database

import org.neo4j.driver.v1.Driver
import org.neo4j.driver.v1.Record
import org.neo4j.driver.v1.StatementResult
import org.slf4j.LoggerFactory

/**
 * Implementation of the Neo4jOperations interface
 */
class Neo4jTemplate(private val driver: Driver) : Neo4jOperations {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(Neo4jTemplate::class.java)
    }

    /**
     * Query the database for exactly one record
     * @param query The query to execute
     * @param params The parameters to use, if any
     * @return the single record that matches
     */
    override fun queryOne(query: String, params: Map<String, Any?>): Record {
        return perform(query, params) {
            it.single()
        }
    }

    /**
     * Execute the given query against the database
     * @param query The query to execute
     * @param params The parameters to the query
     * @return the matching records
     */
    override fun query(query: String, params: Map<String, Any?>): List<Record> {
        return perform(query, params) {
            it.list()
        }
    }

    /**
     * Execute the given query against the database
     * @param query The query to execute
     * @param params The parameters to the query
     * @param transform Function to transform the records
     * @return the matching records
     */
    override fun <T> query(query: String, params: Map<String, Any?>, transform: (Record) -> T): List<T> {
        return perform(query, params) {
            it.list(transform)
        }
    }

    /**
     * Perform the given query against the database
     * @param query The query to execute
     * @param params The parameters to the query
     * @param block Function to transform the result
     * @return the result
     */
    private fun <T> perform(query: String, params: Map<String, Any?>, block: (StatementResult) -> T): T {
        LOG.debug("Executing query: {}, with parameters {}", query, params)
        return driver.session().use { session ->
            val result = session.run(query, params)
            block(result)
        }
    }
}
