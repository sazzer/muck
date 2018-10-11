package uk.co.grahamcox.muck.service.model

/**
 * Representation of a request for a single page from a resultset
 */
data class PageRequest<out SORTFIELD>(
        val sorts : List<Sort<SORTFIELD>> = emptyList(),
        val offset : Long = 0,
        val pageSize : Long = Long.MAX_VALUE
)
