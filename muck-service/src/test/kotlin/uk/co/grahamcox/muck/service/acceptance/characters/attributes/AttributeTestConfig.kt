package uk.co.grahamcox.muck.service.acceptance.characters.attributes

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.service.acceptance.seeder.DatabaseSeeder
import uk.co.grahamcox.muck.service.acceptance.seeder.SeedParam
import java.time.Instant
import java.util.*

/**
 * Configuration for Attribute related details in Acceptance Tests
 */
@Configuration
class AttributeTestConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean("attributeSeeder") {
                DatabaseSeeder(
                        ref(),
                        "CREATE (a:ATTRIBUTE {id:{id}, version:{version}, created:{now}, updated:{now}, name:{name}, description:{description}})",
                        mapOf(
                                "id" to SeedParam { UUID.randomUUID().toString() },
                                "version" to SeedParam { UUID.randomUUID().toString() },
                                "now" to SeedParam { Instant.now().toString() },
                                "name" to SeedParam { "Strength" },
                                "description" to SeedParam { "How Strong I am" }
                        )
                )
            }
        }.initialize(context)
    }
}
