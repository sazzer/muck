package uk.co.grahamcox.muck.service.database

import org.apache.commons.lang3.builder.ToStringBuilder
import org.neo4j.driver.v1.Record
import org.neo4j.driver.v1.StatementResult
import org.neo4j.driver.v1.summary.SummaryCounters
import org.slf4j.LoggerFactory
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.springframework.transaction.support.DefaultTransactionStatus

/**
 * Implementation of the Neo4jOperations interface
 */
@Transactional
class Neo4jTemplate(private val transactionManager: Neo4jTransactionManager) : Neo4jOperations {
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
    @Transactional
    override fun queryOne(query: String, params: Map<String, Any?>): Record {
        return queryOne(query, params) { it }
    }

    /**
     * Query the database for exactly one record
     * @param query The query to execute
     * @param params The parameters to use, if any
     * @param transform Function to transform the records
     * @return the single record that matches
     */
    override fun <T> queryOne(query: String, params: Map<String, Any?>, transform: (Record) -> T): T {
        return perform(query, params) {
            transform(it.single())
        }
    }

    /**
     * Execute the given query against the database
     * @param query The query to execute
     * @param params The parameters to the query
     * @return the matching records
     */
    @Transactional
    override fun query(query: String, params: Map<String, Any?>): List<Record> {
        return query(query, params) { it }
    }

    /**
     * Execute the given query against the database
     * @param query The query to execute
     * @param params The parameters to the query
     * @param transform Function to transform the records
     * @return the matching records
     */
    @Transactional
    override fun <T> query(query: String, params: Map<String, Any?>, transform: (Record) -> T): List<T> {
        return perform(query, params) {
            it.list(transform)
        }
    }

    /**
     * Execute a statement and get the summary of the results
     * @param query The query to execute
     * @param params The parameters to the query
     * @return The result summary
     */
    @Transactional
    override fun execute(query: String, params: Map<String, Any?>): SummaryCounters {
        return perform(query, params) {
            val counters = it.consume().counters()
            LOG.debug("Query result: {}", ToStringBuilder.reflectionToString(counters))
            counters
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
        val transactionDefinition = DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_MANDATORY)
        val transaction = transactionManager.getTransaction(transactionDefinition) as DefaultTransactionStatus
        val neo4jTransaction = transaction.transaction as Neo4jTransactionManager.Neo4jTransaction

        LOG.debug("Executing query: {}, with parameters {} on transaction {}", query, params, neo4jTransaction)

        val result = neo4jTransaction.transaction!!.run(query, params)
        return block(result)
    }
}
