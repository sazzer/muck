package uk.co.grahamcox.muck.service.database

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.neo4j.driver.v1.Driver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import uk.co.grahamcox.muck.service.spring.SpringTestBase

/**
 * Tests for the Neo4J Transaction Manager
 */
@Configuration
@Import(Neo4jTransactionManagerIT.TransactionHelperConfig::class)
internal open class Neo4jTransactionManagerIT : SpringTestBase() {
    /** The direct Neo4J Driver */
    @Autowired
    private lateinit var driver: Driver

    /** The Neo4J Operations with which to make transactional calls to the database */
    @Autowired
    private lateinit var neo4jOperations: Neo4jOperations

    @Autowired
    private lateinit var transactionHelper: TransactionHelper

    /**
     * Test creating a record in a transaction actually commits it
     */
    @Test
    fun testCreateAndCommit() {
        transactionHelper.executeTransactionally {
            neo4jOperations.execute("CREATE (n:TEST)")
        }

        driver.session().use { session ->
            val result = session.run("MATCH (n:TEST) RETURN COUNT(n) AS count")
            val record = result.single()
            Assertions.assertNotNull(record)
            Assertions.assertEquals(1, record.get("count").asInt())
        }
    }

    /**
     * Test creating a record in a transaction and rolling it back
     */
    @Test
    fun testCreateAndRollback() {
        try {
            transactionHelper.executeTransactionally {
                neo4jOperations.execute("CREATE (n:TEST)")
                throw IllegalStateException()
            }
        } catch (e: IllegalStateException) {
            // Expected
        }

        driver.session().use { session ->
            val result = session.run("MATCH (n:TEST) RETURN COUNT(n) AS count")
            val record = result.single()
            Assertions.assertNotNull(record)
            Assertions.assertEquals(0, record.get("count").asInt())
        }
    }

    @Configuration
    class TransactionHelperConfig {

        @Bean
        fun transactionHelper() = TransactionHelper()

    }

    @Transactional
    class TransactionHelper {

        /**
         * Helper to execute a block in a transaction
         */
        open fun executeTransactionally(block: () -> Unit) {
            block()
        }

    }
}
