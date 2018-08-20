package uk.co.grahamcox.muck.service.database

import org.neo4j.driver.v1.Record

/**
 * Interface describing how to interact with the Neo4J Data store
 */
interface Neo4jOperations {
    /**
     * Query the database for exactly one record
     * @param query The query to execute
     * @param params The parameters to use, if any
     * @return the single record that matches
     */
    fun queryOne(query: String, params: Map<String, Any?> = emptyMap()) : Record

    /**
     * Execute the given query against the database
     * @param query The query to execute
     * @param params The parameters to the query
     * @return the matching records
     */
    fun query(query: String, params: Map<String, Any?> = emptyMap()) : List<Record>

    /**
     * Execute the given query against the database
     * @param query The query to execute
     * @param params The parameters to the query
     * @param transform Function to transform the records
     * @return the matching records
     */
    fun <T> query(query: String, params: Map<String, Any?> = emptyMap(), transform: (Record) -> T) : List<T>

}
