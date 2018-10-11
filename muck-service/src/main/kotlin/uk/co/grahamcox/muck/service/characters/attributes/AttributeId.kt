package uk.co.grahamcox.muck.service.characters.attributes

import uk.co.grahamcox.muck.service.model.Id
import java.util.*

/**
 * The ID of an Attribute
 */
data class AttributeId(override val id: UUID) : Id<UUID>
