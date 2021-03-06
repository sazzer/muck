package uk.co.grahamcox.muck.service.acceptance

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import uk.co.grahamcox.muck.service.MuckServiceApplication
import uk.co.grahamcox.muck.service.database.DatabaseCleaner
import uk.co.grahamcox.muck.service.acceptance.requester.Requester
import uk.co.grahamcox.muck.service.authentication.AccessTokenGenerator
import uk.co.grahamcox.muck.service.authentication.rest.AccessTokenSerializer
import uk.co.grahamcox.muck.service.database.Neo4jOperations
import uk.co.grahamcox.muck.service.user.UserId

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
    /** The port that the server is running on */
    @LocalServerPort
    private lateinit var localPort: String

    /** The means to call the database */
    @Autowired
    private lateinit var neo4jOperations: Neo4jOperations

    /** The means to clean the database */
    @Autowired
    private lateinit var databaseCleaner: DatabaseCleaner

    /** The requester that we can use to talk to the API */
    @Autowired
    protected lateinit var requester: Requester

    /** The means to generate an access token */
    @Autowired
    private lateinit var accessTokenGenerator: AccessTokenGenerator

    /** The means to serialize an access token */
    @Autowired
    private lateinit var accessTokenSerializer: AccessTokenSerializer

    /** The rest template to work with */
    @Autowired
    private lateinit var restTemplate: RestTemplate

    /** The Object Mapper to use */
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    /** The mock server to bind the rest template to */
    protected lateinit var mockServer : MockRestServiceServer

    /**
     * Set up the mock server before the tests
     */
    @BeforeEach
    fun setupMockServer() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build()
    }
    
    /**
     * Clear the database before each test
     */
    @BeforeEach
    fun clearDatabase() {
        databaseCleaner.clean()
    }

    /**
     * Clear the requester before each test
     */
    @BeforeEach
    fun clearRequester() {
        requester.reset()
    }

    /**
     * Convert the given string, read as JSON, into a Java Object
     */
    protected fun convertFromJson(json: String): Any =
            objectMapper.readValue(json.toByteArray(Charsets.UTF_8), Any::class.java)

    /**
     * Generate an Access Token valid for the given User ID and use it for future requests
     */
    protected fun authenticatedAs(user: UserId) {
        val accessToken = accessTokenGenerator.generate(user)
        val bearerToken = accessTokenSerializer.serialize(accessToken)

        requester.accessToken = bearerToken
    }

    /**
     * Helper to convert the provided URI to have the correct scheme, host and port for the test service
     */
    protected fun buildUri(input: String) =
            UriComponentsBuilder.fromUriString(input)
                    .scheme("http")
                    .host("localhost")
                    .port(localPort)
                    .build()
                    .toUriString()

}
