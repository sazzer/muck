package uk.co.grahamcox.muck.service.database

import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import org.neo4j.kernel.configuration.BoltConnector
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.FileSystemUtils
import org.springframework.util.SocketUtils
import java.nio.file.Files
import java.nio.file.Path


/**
 * Wrapper around the embedded Neo4J
 */
class EmbeddedNeo4j(val address: String) : InitializingBean, DisposableBean {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(EmbeddedNeo4j::class.java)
    }

    /** The temp directory for the database */
    private lateinit var directory: Path

    /** The actual running database */
    private lateinit var neo4j: GraphDatabaseService

    /**
     * Instantiate the Neo4J database
     */
    override fun afterPropertiesSet() {
        directory = Files.createTempDirectory("muck")

        val bolt = BoltConnector()

        neo4j = GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(directory.toFile())
                .setConfig( bolt.type, "BOLT" )
                .setConfig( bolt.enabled, "true" )
                .setConfig( bolt.address, address )
                .newGraphDatabase()

        LOG.info("Starting Neo4J on {}, based in {}", address, directory)
    }

    /**
     * Destroy the Neo4J database
     */
    override fun destroy() {
        LOG.info("Stopping Neo4J on {}, based in {}", address, directory)

        neo4j.shutdown()

        FileSystemUtils.deleteRecursively(directory)
    }
}

/**
 * Spring configuration for the database
 */
@Configuration
class DatabaseConfig {
    /** The port to listen on. 0 means to pick a random port */
    @Value("\${muck.database.embedded.port:0}")
    private lateinit var port: String

    /**
     * The embedded Neo4J database
     */
    @Bean
    @ConditionalOnProperty(value = "muck.database.embedded.active", havingValue = "true")
    fun neo4j(): EmbeddedNeo4j {
        val address = when (port) {
            "0" -> {
                val randomPort = SocketUtils.findAvailableTcpPort()
                "localhost:$randomPort"
            }
            else -> "localhost:$port"
        }
        return EmbeddedNeo4j(address)
    }
}
