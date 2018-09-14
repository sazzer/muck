package uk.co.grahamcox.muck.service.home.rest

import com.fasterxml.jackson.annotation.JsonProperty
import uk.co.grahamcox.muck.service.rest.hal.Link

/**
 * Links for the Home Model
 */
data class HomeLinks(
        val self: Link,
        val externalAuthenticationServices: Link,
        val user: Link
)

/**
 * The API Model representing the Home Document
 */
data class HomeModel(
        @JsonProperty("_links") val links: HomeLinks
)
