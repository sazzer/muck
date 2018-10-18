package uk.co.grahamcox.muck.service.characters.attributes.rest

import java.util.*

/**
 * REST Model representing a single Attribute
 */
data class AttributeModel(
        val id: UUID,
        val name: String,
        val description: String
)
