package uk.co.grahamcox.muck.service.model

import java.time.Instant

/**
 * Representation of the identity of some resource
 */
data class Identity<out ID : Id<*>>(
        val id: ID,
        val version: Long,
        val created: Instant,
        val updated: Instant
)
