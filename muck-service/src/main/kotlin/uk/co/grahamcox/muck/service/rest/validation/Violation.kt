package uk.co.grahamcox.muck.service.rest.validation

/**
 * Representation of a single constraint violation
 */
data class Violation(
        val field: String,
        val title: String,
        val type: String,
        val value: Any?
)
