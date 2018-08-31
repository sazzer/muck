package uk.co.grahamcox.muck.service.acceptance.requester

import org.springframework.web.util.UriComponentsBuilder

/**
 * Helper to convert the incoming URI String to one without a port component
 */
fun convertReturnedUri(input: Any?): String? {
    return when (input) {
        is String -> UriComponentsBuilder.fromUriString(input!!)
                .port(-1)
                .build()
                .toUriString()
        null -> throw NullPointerException("Provided URI was null")
        else -> throw IllegalArgumentException("Provided URI was not a string")
    }
}
