package uk.co.grahamcox.muck.e2e.database

import cucumber.api.java.Before
import org.neo4j.driver.v1.Driver
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * Cucumber steps for dealing with the database
 */
class DatabaseSteps {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(DatabaseSteps::class.java)
    }

    /** The database connection */
    @Autowired
    private lateinit var driver: Driver

    /**
     * Clean the database before each test
     */
    @Before
    fun cleanDatabase() {
        driver.session().use { session ->
            val result = session.run("MATCH (n) DETACH DELETE n")
            LOG.debug("Deleted {} nodes and {} relationships",
                    result.summary().counters().nodesDeleted(),
                    result.summary().counters().relationshipsDeleted())
        }
    }
}
