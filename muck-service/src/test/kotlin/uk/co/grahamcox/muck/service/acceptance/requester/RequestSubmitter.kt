package uk.co.grahamcox.muck.service.acceptance.requester

import org.springframework.http.HttpMethod
import org.springframework.web.util.UriComponentsBuilder

/**
 * Configuration of a single field to use in a request
 */
data class RequestFieldConfig(
        val fieldName: String,
        val valueConversion: (String) -> Any = { it -> it }
)

/**
 * Wrapper around making a request
 */
class RequestSubmitter(
        private val requester: Requester,
        private val uri: String,
        private val method: HttpMethod,
        private val requestUriFields: Map<String, RequestFieldConfig>,
        private val requestBodyFields: Map<String, RequestFieldConfig>
) {
    /**
     * Make a request as defined by the provided input fields
     * @param input The inputs to use
     */
    fun makeRequest(input: Map<String, String> = mapOf()) {
        val uriFields = requestUriFields
                .filter { input.containsKey(it.key) }
                .toList()
                .map { (key, config) ->
                    config.fieldName to config.valueConversion(input[key]!!)
                }
                .toMap()

        val bodyFields = requestBodyFields
                .filter { input.containsKey(it.key) }
                .toList()
                .map { (key, config) ->
                    config.fieldName to config.valueConversion(input[key]!!)
                }
                .toMap()

        val realUri = UriComponentsBuilder.fromUriString(uri)
                .buildAndExpand(uriFields)
                .toUriString()

        requester.makeRequest(realUri, method, bodyFields)
    }
}
