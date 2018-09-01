package uk.co.grahamcox.muck.service.acceptance

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.util.UriComponentsBuilder
import uk.co.grahamcox.muck.service.MuckServiceApplication
import uk.co.grahamcox.muck.service.database.DatabaseCleaner
import uk.co.grahamcox.muck.service.acceptance.requester.Requester
import uk.co.grahamcox.muck.service.database.Neo4jOperations

/**
 * Base class for Spring based tests
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [MuckServiceApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(
        AcceptanceTestConfig::class
)
class AcceptanceTestBase {

    /** The means to call the database */
    @Autowired
    private lateinit var neo4jOperations: Neo4jOperations

    /** The means to clean the database */
    @Autowired
    private lateinit var databaseCleaner: DatabaseCleaner

    /** The requester that we can use to talk to the API */
    @Autowired
    protected lateinit var requester: Requester

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
    protected fun execute(query: String, parameters: Map<String, Any?> = emptyMap()) {
        neo4jOperations.execute(query, parameters)
    }

    /**
     * Helper to convert the incoming URI String to one without a port component
     */
    protected fun convertReturnedUri(input: Any?): String? {
        return when (input) {
            is String -> UriComponentsBuilder.fromUriString(input!!)
                    .port(-1)
                    .build()
                    .toUriString()
            null -> throw NullPointerException("Provided URI was null")
            else -> throw IllegalArgumentException("Provided URI was not a string")
        }
    }

}
