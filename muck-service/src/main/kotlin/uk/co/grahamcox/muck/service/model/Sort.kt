package uk.co.grahamcox.muck.service.model

/**
 * Description of a single sort to apply
 */
data class Sort<out FIELD>(
        val field : FIELD,
        val direction: SortDirection
)
