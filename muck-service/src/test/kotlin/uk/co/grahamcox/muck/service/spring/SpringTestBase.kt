package uk.co.grahamcox.muck.service.spring

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.neo4j.driver.v1.Driver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import uk.co.grahamcox.muck.service.MuckServiceApplication
import uk.co.grahamcox.muck.service.acceptance.database.DatabaseCleaner

/**
 * Base class for Spring based tests
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [MuckServiceApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SpringTestBase {
    /** The database cleaner */
    @Autowired
    private lateinit var databaseCleaner: DatabaseCleaner

    /**
     * Clear the database before each test
     */
    @BeforeEach
    fun clearDatabase() {
        databaseCleaner.clean()
    }

    /**
     * Execute the given query against the database
     */
    fun execute(query: String, parameters: Map<String, Any?> = emptyMap()) {
    }
}
