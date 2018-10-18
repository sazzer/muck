package uk.co.grahamcox.muck.service.rest

/**
 * Representation of a single page of some resource
 */
data class PageModel<T>(
        val data: List<T>,
        val offset: Int,
        val total: Int
) {
    /** Helper to indicate if there is a previous page */
    val hasPrevious = (offset > 0)

    /** Helper to indicate if there is a next page */
    val hasNext = (offset + data.size < total)
}
