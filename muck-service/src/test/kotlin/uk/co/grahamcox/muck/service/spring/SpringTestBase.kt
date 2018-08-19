package uk.co.grahamcox.muck.service.spring

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.neo4j.ogm.cypher.query.DefaultGraphModelRequest
import org.neo4j.ogm.session.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import uk.co.grahamcox.muck.service.MuckServiceApplication

/**
 * Base class for Spring based tests
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [MuckServiceApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SpringTestBase {
    /** The session factory to interact with the database */
    @Autowired
    private lateinit var sessionFactory: SessionFactory

    /**
     * Clear the database before each test
     */
    @BeforeEach
    fun clearDatabase() {
        sessionFactory.openSession().purgeDatabase()
    }

    /**
     * Execute the given query against the database
     */
    fun execute(query: String, parameters: Map<String, Any?> = emptyMap()) {
        val request = DefaultGraphModelRequest(query, parameters)
        sessionFactory.driver.request().execute(request)
    }
}
