package uk.co.grahamcox.muck.service.characters.attributes.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import uk.co.grahamcox.muck.service.characters.attributes.AttributeId
import uk.co.grahamcox.muck.service.characters.attributes.AttributeResource
import uk.co.grahamcox.muck.service.characters.attributes.AttributeRetriever
import uk.co.grahamcox.muck.service.database.ResourceNotFoundException
import uk.co.grahamcox.muck.service.rest.Problem
import java.net.URI
import java.util.*

/**
 * Controller for interacting with attribute records
 */
@RestController
@RequestMapping("/api/attributes")
class AttributeController(private val attributeRetriever: AttributeRetriever) {
    /**
     * Handle interacting with an unknown attribute
     */
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleUnknownAttribute() =
            Problem(
                    type = URI("tag:grahamcox.co.uk,2018,problems/attributes/unknown-attribute"),
                    title = "The requested attribute was not found",
                    statusCode = HttpStatus.NOT_FOUND
            )

    /**
     * Get the details of an attribute by its unique ID
     */
    @RequestMapping("/{id}", method = [RequestMethod.GET])
    fun getAttribute(@PathVariable("id") rawId: UUID) : AttributeModel {
        val attribute = attributeRetriever.getById(AttributeId(rawId))

        return translateAttributeResource(attribute)
    }

    /**
     * Translate an Attribute into the REST Model version
     */
    private fun translateAttributeResource(attribute: AttributeResource) =
            AttributeModel(
                    id = attribute.identity.id.id,
                    name = attribute.data.name,
                    description = attribute.data.description
            )
}
