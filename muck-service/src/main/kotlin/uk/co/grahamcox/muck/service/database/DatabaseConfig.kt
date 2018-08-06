package uk.co.grahamcox.muck.service.database

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.BeanCreationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres
import ru.yandex.qatools.embed.postgresql.distribution.Version

/**
 * Wrapper around the Postgres Server
 */
class PostgresWrapper {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(PostgresWrapper::class.java)
    }

    /** The postgres server  */
    private val postgres = EmbeddedPostgres(Version.V10_3)

    @Value("\${muck.database.embedded.port:0}")
    private lateinit var port: Integer

    /** The database connection URL */
    lateinit var url: String

    /**
     * Start the server
     */
    fun start() {
        url = when (port) {
            null -> throw BeanCreationException("No port number set")
            Integer(0) -> postgres.start()
            else -> postgres.start("localhost", port.toInt(), "muck")
        }
        LOG.debug("Started Postgres server on {}", url)
    }

    /**
     * Stop the server
     */
    fun stop() {
        postgres.stop()
        LOG.debug("Stopping Postgres server on {}", url)
    }
}

/**
 * Spring configuration for the database
 */
@Configuration
@ConditionalOnProperty(value = "muck.database.embedded.active", havingValue = "true")
class DatabaseConfig {
    /**
     * The Embedded Postgres server
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    fun embeddedPostgres() = PostgresWrapper()

    /**
     * The data source to use
     */
    @Bean
    fun datasource(postgres: PostgresWrapper) = DataSourceBuilder.create()
            .url(postgres.url)
            .build()
}
