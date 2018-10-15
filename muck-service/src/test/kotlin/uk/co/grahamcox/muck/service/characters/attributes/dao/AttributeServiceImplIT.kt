package uk.co.grahamcox.muck.service.characters.attributes.dao

import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.muck.service.characters.attributes.AttributeData
import uk.co.grahamcox.muck.service.characters.attributes.AttributeId
import uk.co.grahamcox.muck.service.characters.attributes.AttributeResource
import uk.co.grahamcox.muck.service.characters.attributes.AttributeSort
import uk.co.grahamcox.muck.service.database.Neo4jOperations
import uk.co.grahamcox.muck.service.database.ResourceNotFoundException
import uk.co.grahamcox.muck.service.model.*
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
     * Test getting a list of attributes from an empty database
     */
    @TestFactory
    fun testListNoAttributes() = listOf(
            PageRequest(),
            PageRequest(offset = 10),
            PageRequest(pageSize = 10),
            PageRequest(pageSize = 0),
            PageRequest(offset = 10, pageSize = 10),
            PageRequest(offset = 0, pageSize = 0),
            PageRequest(sorts = listOf(Sort(AttributeSort.NAME, SortDirection.DESCENDING)))
    ).map {
        DynamicTest.dynamicTest(it.toString()) {
            val page = attributeServiceImpl.list(it)
            Assertions.assertAll(
                    Executable { Assertions.assertTrue(page.data.isEmpty()) },
                    Executable { Assertions.assertEquals(0, page.total) },
                    Executable { Assertions.assertEquals(it.offset, page.offset) }
            )
        }
    }

    /**
     * Test getting a list of attributes from database of exactly one
     */
    @TestFactory
    fun testListOneAttributes(): List<DynamicTest> {
        val str = createAttribute(AttributeId(UUID.randomUUID()),
                UUID.randomUUID(),
                Instant.now(),
                "Strength",
                "How Strong I am")

        return mapOf(
                PageRequest<AttributeSort>() to Page(listOf(str), 0, 1),
                PageRequest<AttributeSort>(offset = 10) to Page(emptyList(), 10, 1),
                PageRequest<AttributeSort>(pageSize = 10) to Page(listOf(str), 0, 1),
                PageRequest<AttributeSort>(pageSize = 0) to Page(emptyList(), 0, 1),
                PageRequest<AttributeSort>(offset = 10, pageSize = 10) to Page(emptyList(), 10, 1),
                PageRequest<AttributeSort>(offset = 0, pageSize = 0) to Page(emptyList(), 0, 1),
                PageRequest(sorts = listOf(Sort(AttributeSort.NAME, SortDirection.DESCENDING))) to Page(listOf(str), 0, 1)
        ).toList()
                .map { (request, expected) ->
                    DynamicTest.dynamicTest(request.toString()) {
                        val page = attributeServiceImpl.list(request)

                        Assertions.assertEquals(expected, page)
                    }
                }
    }

    /**
     * Test getting a list of attributes from database of several
     */
    @TestFactory
    fun testListManyAttributes(): List<DynamicTest> {
        val str = createAttribute(AttributeId(UUID.randomUUID()),
                UUID.randomUUID(),
                Instant.now(),
                "Strength",
                "How Strong I am")
        val dex = createAttribute(AttributeId(UUID.randomUUID()),
                UUID.randomUUID(),
                Instant.now(),
                "Dexterity",
                "How Dexterous I am")
        val luck = createAttribute(AttributeId(UUID.randomUUID()),
                UUID.randomUUID(),
                Instant.now(),
                "Luck",
                "How Lucky I am")

        return mapOf(
                PageRequest<AttributeSort>() to Page(listOf(dex, luck, str), 0, 3),
                PageRequest<AttributeSort>(offset = 10) to Page(emptyList(), 10, 3),
                PageRequest<AttributeSort>(pageSize = 10) to Page(listOf(dex, luck, str), 0, 3),
                PageRequest<AttributeSort>(pageSize = 0) to Page(emptyList(), 0, 3),
                PageRequest<AttributeSort>(offset = 10, pageSize = 10) to Page(emptyList(), 10, 3),
                PageRequest<AttributeSort>(offset = 0, pageSize = 0) to Page(emptyList(), 0, 3),
                PageRequest(sorts = listOf(Sort(AttributeSort.NAME, SortDirection.ASCENDING))) to Page(listOf(dex, luck, str), 0, 3),
                PageRequest(sorts = listOf(Sort(AttributeSort.NAME, SortDirection.DESCENDING))) to Page(listOf(str, luck, dex), 0, 3)
        ).toList()
                .map { (request, expected) ->
                    DynamicTest.dynamicTest(request.toString()) {
                        val page = attributeServiceImpl.list(request)

                        Assertions.assertEquals(expected, page)
                    }
                }
    }

    /**
     * Create the attribute record to use in the tests
     */
    private fun createAttribute(attributeId: AttributeId, version: UUID, now: Instant, name: String, description: String): AttributeResource {
        execute("CREATE (u:ATTRIBUTE {id:{id}, version:{version}, created:{now}, updated:{now}, name:{name}, description:{description}})",
                mapOf(
                        "id" to attributeId.id.toString(),
                        "version" to version.toString(),
                        "now" to now.toString(),
                        "name" to name,
                        "description" to description
                ))

        return AttributeResource(
                identity = Identity(
                        id = attributeId,
                        version = version,
                        created = now,
                        updated = now
                ),
                data = AttributeData(
                        name = name,
                        description = description
                )
        )

    }
}
