package uk.co.grahamcox.muck.service.database

import org.neo4j.driver.v1.Driver
import org.springframework.boot.actuate.health.AbstractHealthIndicator
import org.springframework.boot.actuate.health.Health

/**
 * The Neo4J Healthcheck to use
 */
class Neo4jHealthcheck(private val driver: Driver) : AbstractHealthIndicator() {
    companion object {
        /** The query to run for the healthcheck */
        private const val QUERY = "MATCH (n) RETURN count(*) as count"
    }

    /**
     * Actually perform the healthcheck
     */
    override fun doHealthCheck(builder: Health.Builder) {
        val result = driver.query(QUERY)

        val count = result.single().get("count").asInt()

        builder.up()
                .withDetail("Nodes", count)
                .build()
    }
}
