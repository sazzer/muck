package uk.co.grahamcox.muck.service.rest.hal

import java.net.URI

/**
 * Representation of a single link in a HAL document
 */
data class Link(
        val href: String,
        val templated: Boolean? = false,
        val type: String? = "application/hal+json",
        val deprecation: String? = null,
        val name: String? = null,
        val profile: URI? = null,
        val title: String? = null,
        val hreflang: String? = null
)
