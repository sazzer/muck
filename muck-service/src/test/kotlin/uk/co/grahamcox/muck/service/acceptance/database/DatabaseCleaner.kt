package uk.co.grahamcox.muck.service.acceptance.database

import org.slf4j.LoggerFactory

/**
 * Mechanism by which we can clean the database of all data
 * DO NOT USE IN A REAL SITUATION
 */
class DatabaseCleaner(
) {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(DatabaseCleaner::class.java)
    }

    /**
     * Clean all of the tables in the database
     */
    fun clean() {
        LOG.debug("Cleaning all data from database")
    }
}
