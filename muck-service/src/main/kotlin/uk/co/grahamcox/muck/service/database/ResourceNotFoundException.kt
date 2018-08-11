package uk.co.grahamcox.muck.service.database

import uk.co.grahamcox.muck.service.model.Id

/**
 * Indication that a resource could not be found
 */
class ResourceNotFoundException(val id: Id) : RuntimeException("Resource not found: $id")
