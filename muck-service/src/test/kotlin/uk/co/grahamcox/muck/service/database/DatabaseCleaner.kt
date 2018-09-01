package uk.co.grahamcox.muck.service.database

import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

/**
 * Mechanism by which we can clean the database of all data
 * DO NOT USE IN A REAL SITUATION
 */
open class DatabaseCleaner(
        private val neo4jOperations: Neo4jOperations
) {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(DatabaseCleaner::class.java)
    }

    /**
     * Clean all of the tables in the database
     */
    @Transactional
    open fun clean() {
        LOG.debug("Cleaning all data from database")
        val summaryCounters = neo4jOperations.execute("MATCH (n) DETACH DELETE n")
        LOG.debug("Deleted {} nodes and {} relationships",
                summaryCounters.nodesDeleted(),
                summaryCounters.relationshipsDeleted())
    }
}
