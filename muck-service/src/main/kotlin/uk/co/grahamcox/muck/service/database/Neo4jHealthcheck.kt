package uk.co.grahamcox.muck.service.database

import org.springframework.boot.actuate.health.AbstractHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.transaction.annotation.Transactional

/**
 * The Neo4J Healthcheck to use
 */
@Transactional
class Neo4jHealthcheck(private val neo4jOperations: Neo4jOperations) : AbstractHealthIndicator() {
    companion object {
        /** The query to run for the healthcheck */
        private const val QUERY = "MATCH (n) RETURN count(*) as count"
    }

    /**
     * Actually perform the healthcheck
     */
    @Transactional
    override fun doHealthCheck(builder: Health.Builder) {
        val count = neo4jOperations.queryOne(QUERY).get("count").asInt()

        builder.up()
                .withDetail("Nodes", count)
                .build()
    }
}
