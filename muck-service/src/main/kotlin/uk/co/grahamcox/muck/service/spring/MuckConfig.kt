package uk.co.grahamcox.muck.service.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import uk.co.grahamcox.muck.service.database.DatabaseConfig
import uk.co.grahamcox.muck.service.user.spring.UserConfig

/**
 * Core configuration of MucK
 */
@Configuration
@Import(
        DatabaseConfig::class,
        UserConfig::class
)
class MuckConfig
