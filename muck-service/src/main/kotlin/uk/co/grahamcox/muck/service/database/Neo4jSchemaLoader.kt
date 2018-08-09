package uk.co.grahamcox.muck.service.database

import org.neo4j.driver.v1.Driver
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.util.StreamUtils

/**
 * Set up the Schema in the Neo4J database
 */
class Neo4jSchemaLoader(
        private val resourcePatternResolver: ResourcePatternResolver,
        private val resourcePattern: String,
        private val driver: Driver): InitializingBean {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(Neo4jSchemaLoader::class.java)
    }

    /**
     * Load the schema definitions and execute them
     */
    override fun afterPropertiesSet() {
        driver.exec { session ->
            session.writeTransaction { transaction ->
                resourcePatternResolver.getResources(resourcePattern).forEach { resource ->
                    LOG.debug("Loading schema from {}", resource)
                    val schema = StreamUtils.copyToString(resource.inputStream, Charsets.UTF_8)

                    schema.split(";")
                            .map { it.trim() }
                            .filter { it.isNotBlank() }
                            .forEach { query ->
                                LOG.debug("Executing schema query: {}", query)
                                transaction.run(query)
                            }
                }
            }
        }
    }
}
