package uk.co.grahamcox.muck.service.database

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration

/**
 * Spring configuration for the database
 */
@Configuration
@ConditionalOnProperty(value = "muck.database.embedded.active", havingValue = "true")
class DatabaseConfig {
}
