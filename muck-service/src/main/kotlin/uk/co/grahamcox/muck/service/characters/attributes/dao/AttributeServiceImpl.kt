package uk.co.grahamcox.muck.service.characters.attributes.dao

import org.neo4j.driver.v1.Record
import org.neo4j.driver.v1.exceptions.NoSuchRecordException
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import uk.co.grahamcox.muck.service.characters.attributes.*
import uk.co.grahamcox.muck.service.database.Neo4jOperations
import uk.co.grahamcox.muck.service.database.ResourceNotFoundException
import uk.co.grahamcox.muck.service.model.Identity
import uk.co.grahamcox.muck.service.model.Page
import uk.co.grahamcox.muck.service.model.PageRequest
import java.time.Instant
import java.util.*

/**
 * Implementation of the Attribute Retriever in terms of the Neo4J Database
 */
@Transactional
class AttributeServiceImpl(
        private val neo4jOperations: Neo4jOperations
): AttributeRetriever {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(AttributeServiceImpl::class.java)
    }

    /**
     * Get the single Attribute with the given ID
     * @param id The ID of the attribute
     * @return the attribute
     */
    override fun getById(id: AttributeId): AttributeResource {
        val query = """
            MATCH
                (attribute:ATTRIBUTE)
            WHERE
                attribute.id = {id}
            RETURN
                attribute"""

        LOG.debug("Loading attribute with ID: {}", id)

        val result = try {
            neo4jOperations.queryOne(query,
                    mapOf("id" to id.id.toString()),
                    ::parseAttributeRecord)
        } catch (e: NoSuchRecordException) {
            throw ResourceNotFoundException(id)
        }
        LOG.debug("Attribute record: {}", result)

        return result
    }

    /**
     * Parse a single Neo4J record that represents an Attribute record
     */
    private fun parseAttributeRecord(record: Record): AttributeResource {
        val attribute = record.get("attribute").asNode()

        return AttributeResource(
                identity = Identity(
                        id = AttributeId(UUID.fromString(attribute.get("id").asString())),
                        version = UUID.fromString(attribute.get("version").asString()),
                        created = Instant.parse(attribute.get("created").asString()),
                        updated = Instant.parse(attribute.get("updated").asString())
                ),
                data = AttributeData(
                        name = attribute.get("name").asString(),
                        description = attribute.get("description").asString()
                )
        )
    }

    /**
     * Get a list of all the attributes
     * @param pageRequest The pagination details
     * @return the requested page of attributes
     */
    override fun list(pageRequest: PageRequest<AttributeSort>): Page<AttributeResource> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
