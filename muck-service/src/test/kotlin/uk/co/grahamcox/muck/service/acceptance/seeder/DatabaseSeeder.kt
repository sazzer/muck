package uk.co.grahamcox.muck.service.acceptance.seeder

import org.slf4j.LoggerFactory
import uk.co.grahamcox.muck.service.database.Neo4jOperations

/**
 * A parameter for seeding data
 */
data class SeedParam(
        val defaulter: () -> Any?
)

/**
 * Mechanism through which we can seed records into the database
 */
class DatabaseSeeder(
        private val neo4jOperations: Neo4jOperations,
        private val query: String,
        private val seedParams: Map<String, SeedParam>
) {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(DatabaseSeeder::class.java)
    }

    /**
     * Seed a record with the given data
     */
    fun seed(params: Map<String, Any?>) {
        val unknownParams = params.keys.filterNot(seedParams::containsKey)
        if (unknownParams.isNotEmpty()) {
            throw IllegalArgumentException("Unknown params provided: $unknownParams")
        }

        val actualParams = seedParams.map { (key, value) ->
            val provided = params[key]

            key to (provided ?: value.defaulter.invoke())
        }.toMap()

        LOG.debug("Executing query {} with determined params {}", query, actualParams)
        val result = neo4jOperations.execute(query, actualParams)
        LOG.debug("Result of query: {}", result)
    }
}
