package uk.co.grahamcox.muck.service.rest

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.HttpStatus
import java.net.URI

/**
 * Representation of a problem in a REST call
 */
data class Problem(
        val type: URI,
        val title: String,
        @JsonIgnore val statusCode: HttpStatus,
        val instance: URI? = null,
        val detail: String? = null,
        @get:JsonAnyGetter val extraData: Map<String, Any> = emptyMap()
) {
    val status = statusCode.value()
}
