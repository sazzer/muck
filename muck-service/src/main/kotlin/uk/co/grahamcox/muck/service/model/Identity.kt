package uk.co.grahamcox.muck.service.model

import java.time.Instant

/**
 * Representation of the identity of some resource
 */
class Identity<out ID : Id>(
        val id: ID,
        val version: String,
        val created: Instant,
        val updated: Instant
)
