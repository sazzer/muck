package uk.co.grahamcox.muck.service.model

/**
 * Representation of a single page of data
 */
data class Page<out DATA>(
        val data: List<DATA>,
        val offset: Int,
        val total: Int
)
