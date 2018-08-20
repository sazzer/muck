package uk.co.grahamcox.muck.service.database

import org.neo4j.driver.v1.Driver
import org.neo4j.driver.v1.GraphDatabase
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import org.neo4j.kernel.configuration.BoltConnector
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.AdviceMode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
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
@EnableTransactionManagement(proxyTargetClass = true, mode = AdviceMode.PROXY)
class DatabaseConfig {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(DatabaseConfig::class.java)
    }

    /**
     * The embedded Neo4J database
     */
    @Bean
    @ConditionalOnProperty(value = ["muck.database.embedded.active"], havingValue = "true")
    fun neo4j(@Value("\${muck.database.embedded.port:0}") port: String): EmbeddedNeo4j {
        val address = when (port) {
            "0" -> {
                val randomPort = SocketUtils.findAvailableTcpPort()
                "localhost:$randomPort"
            }
            else -> "localhost:$port"
        }
        return EmbeddedNeo4j(address)
    }

    /**
     * Bolt Connection for the Embedded Neo4J
     */
    @Bean
    @ConditionalOnProperty(value = ["muck.database.embedded.active"], havingValue = "true")
    fun embeddedDriver(embeddedNeo4j: EmbeddedNeo4j) = buildDriver("bolt://${embeddedNeo4j.address}")

    /**
     * Bolt Connection for the External Neo4J
     */
    @Bean
    @ConditionalOnProperty(value = ["neo4j.address"])
    fun externalDriver(@Value("\${neo4j.address}") address: String) = buildDriver(address)

    /**
     * Build the Neo4J driver for the given address
     */
    private fun buildDriver(address: String): Driver {
        LOG.info("Creating Neo4J connection to {}", address)
        return GraphDatabase.driver(address)
    }

    /**
     * Build the Neo4J Template to use
     */
    @Bean
    fun neo4jTemplate(transactionManager: Neo4jTransactionManager) = Neo4jTemplate(transactionManager)

    /**
     * Build the Neo4J Transaction Manager to use
     */
    @Bean("transactionManager")
    fun neo4jTransactionManager(driver: Driver): Neo4jTransactionManager {
        return Neo4jTransactionManager(driver)
    }

    /**
     * The Neo4J Healthcheck
     */
    @Bean
    fun neo4jHealthcheck(neo4jOperations: Neo4jOperations) = Neo4jHealthcheck(neo4jOperations)
}
