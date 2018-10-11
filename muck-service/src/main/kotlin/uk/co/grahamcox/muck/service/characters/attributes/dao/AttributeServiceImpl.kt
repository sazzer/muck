package uk.co.grahamcox.muck.service.characters.attributes.dao

import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import uk.co.grahamcox.muck.service.characters.attributes.AttributeId
import uk.co.grahamcox.muck.service.characters.attributes.AttributeResource
import uk.co.grahamcox.muck.service.characters.attributes.AttributeRetriever
import uk.co.grahamcox.muck.service.characters.attributes.AttributeSort
import uk.co.grahamcox.muck.service.database.Neo4jOperations
import uk.co.grahamcox.muck.service.model.Page
import uk.co.grahamcox.muck.service.model.PageRequest

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
