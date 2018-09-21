package uk.co.grahamcox.muck.service.database

import uk.co.grahamcox.muck.service.model.Id

/**
 * Indication that a resource had a different version to that expected
 */
class OptimisticLockException(val id: Id<*>) : RuntimeException("Resource $id had unexpected version")
