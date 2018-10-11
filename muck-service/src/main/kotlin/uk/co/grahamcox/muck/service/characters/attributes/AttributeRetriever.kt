package uk.co.grahamcox.muck.service.characters.attributes

import uk.co.grahamcox.muck.service.model.Page
import uk.co.grahamcox.muck.service.model.PageRequest

/**
 * Interface describing how to retrieve attributes
 */
interface AttributeRetriever {
    /**
     * Get the single Attribute with the given ID
     * @param id The ID of the attribute
     * @return the attribute
     */
    fun getById(id: AttributeId) : AttributeResource

    /**
     * Get a list of all the attributes
     * @param pageRequest The pagination details
     * @return the requested page of attributes
     */
    fun list(pageRequest: PageRequest<AttributeSort>) : Page<AttributeResource>
}
