package uk.co.grahamcox.muck.service.authentication.external.rest

import com.fasterxml.jackson.annotation.JsonProperty
import uk.co.grahamcox.muck.service.rest.hal.Link

/**
 * The links for an External Authentication Services response
 */
data class ExternalAthenticationServicesLinks(
        val self: Link,
        val services: List<Link>
)

/**
 * The API model representing the External Authentication Services
 */
data class ExternalAuthenticationServicesModel(
    @JsonProperty("_links") val links: ExternalAthenticationServicesLinks,
    val services: List<String>
)
