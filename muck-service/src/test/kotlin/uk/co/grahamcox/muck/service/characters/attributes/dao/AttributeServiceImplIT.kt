package uk.co.grahamcox.muck.service.characters.attributes.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.muck.service.characters.attributes.AttributeId
import uk.co.grahamcox.muck.service.database.Neo4jOperations
import uk.co.grahamcox.muck.service.database.ResourceNotFoundException
import uk.co.grahamcox.muck.service.spring.SpringTestBase
import java.time.Instant
import java.util.*

/**
 * Integration tests for the Attribute Service
 */
class AttributeServiceImplIT : SpringTestBase() {
    /** The database connection */
    @Autowired
    private lateinit var neo4jOperations: Neo4jOperations

    /** The test subject */
    private lateinit var attributeServiceImpl: AttributeServiceImpl

    /**
     * Set up the test subject
     */
    @BeforeEach
    fun setup() {
        attributeServiceImpl = AttributeServiceImpl(neo4jOperations)
    }

    /**
     * Test getting an unknown attribute by ID
     */
    @Test
    fun testGetUnknownById() {
        val attributeId = AttributeId(UUID.randomUUID())

        val e = Assertions.assertThrows(ResourceNotFoundException::class.java) {
            attributeServiceImpl.getById(attributeId)
        }

        Assertions.assertEquals(attributeId, e.id)
    }

    /**
     * Test getting a known attribute
     */
    @Test
    fun testGetKnownById() {
        val attributeId = AttributeId(UUID.randomUUID())
        val version = UUID.randomUUID()
        val now = Instant.now()
        createAttribute(attributeId, version, now, "Strength", "How Strong I am")

        val attribute = attributeServiceImpl.getById(attributeId)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(attributeId, attribute.identity.id) },
                Executable { Assertions.assertEquals(version, attribute.identity.version) },
                Executable { Assertions.assertEquals(now, attribute.identity.created) },
                Executable { Assertions.assertEquals(now, attribute.identity.updated) },

                Executable { Assertions.assertEquals("Strength", attribute.data.name) },
                Executable { Assertions.assertEquals("How Strong I am", attribute.data.description) }
        )
    }

    /**
     * Create the attribute record to use in the tests
     */
    private fun createAttribute(attributeId: AttributeId, version: UUID, now: Instant, name: String, description: String) {
        execute("CREATE (u:ATTRIBUTE {id:{id}, version:{version}, created:{now}, updated:{now}, name:{name}, description:{description}})",
                mapOf(
                        "id" to attributeId.id.toString(),
                        "version" to version.toString(),
                        "now" to now.toString(),
                        "name" to name,
                        "description" to description
                ))
    }
}
