package uk.co.grahamcox.muck.service.acceptance.requester

import org.apache.commons.jxpath.JXPathContext
import org.springframework.http.ResponseEntity

/**
 * Response from the Requester that makes checking the contents easier
 * @property response The actual response
 */
data class Response(
        val response: ResponseEntity<out Any>
) {
    /** The HTTP Status Code returned */
    val statusCode = response.statusCode

    /** The HTTP Response Headers */
    val headers = response.headers

    /** The JXPath Context for interrogating the response */
    val context = JXPathContext.newContext(response)

    /** Get the value from the response for the given JXPath */
    fun getValue(path: String) = context.getValue(path)
}
