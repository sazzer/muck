package uk.co.grahamcox.muck.service.model

/**
 * Representation of some ID
 */
interface Id<out T> {
    val id: T
}
