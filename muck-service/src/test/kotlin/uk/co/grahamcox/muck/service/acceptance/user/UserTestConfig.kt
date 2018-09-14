package uk.co.grahamcox.muck.service.acceptance.user

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.muck.service.acceptance.seeder.DatabaseSeeder
import uk.co.grahamcox.muck.service.acceptance.seeder.SeedParam
import java.time.Instant
import java.util.*

/**
 * Configuration for User related details in Acceptance Tests
 */
@Configuration
class UserTestConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean("userSeeder") {
                DatabaseSeeder(
                        ref(),
                        "CREATE (u:USER {id:{id}, version:{version}, created:{now}, updated:{now}, email:{email}, displayName:{displayName}})",
                        mapOf(
                                "id" to SeedParam { UUID.randomUUID().toString() },
                                "version" to SeedParam { UUID.randomUUID().toString() },
                                "now" to SeedParam { Instant.now().toString() },
                                "email" to SeedParam { "testuser@example.com" },
                                "displayName" to SeedParam { "Test User" }
                        )
                )
            }
            bean("userProviderSeeder") {
                DatabaseSeeder(
                        ref(),
                        "MATCH (u:USER {id:{userId}}) MERGE (p:LOGIN_PROVIDER {id:{provider}}) CREATE (u)-[:LOGIN {providerId:{providerId}, displayName:{displayName}}]->(p)",
                        mapOf(
                                "userId" to SeedParam { TODO() },
                                "provider" to SeedParam { TODO() },
                                "providerId" to SeedParam { TODO() },
                                "displayName" to SeedParam { "Test User" }
                        )
                )
            }
        }.initialize(context)
    }
}
