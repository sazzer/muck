package uk.co.grahamcox.muck.service.model

/**
 * Representation of some resource
 */
class Resource<out ID: Id, out DATA>(
        val identity: Identity<ID>,
        val data: DATA
)
