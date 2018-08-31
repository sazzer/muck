package uk.co.grahamcox.muck.service.acceptance.requester

import io.cucumber.datatable.DataTable
import org.apache.commons.jxpath.JXPathNotFoundException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.function.Executable

/**
 * Configuration of a single field to match
 */
data class ResponseFieldConfig(
        val fieldPath: String,
        val expectedConversion: (String) -> Any? = { it -> it },
        val actualConversion: (Any?) -> Any? = { it -> it },
        val comparison: (Any?, Any?) -> Unit = { expected, actual -> Assertions.assertEquals(expected, actual) }
)

/**
 * Means to match the response received from the system
 */
class ResponseMatcher(
        private val requester: Requester,
        private val matchers: Map<String, ResponseFieldConfig>
) {
    /**
     * Check if the last response in the Requester matches the expected values
     */
    fun match(dataTable: DataTable) = match(dataTable.asMap(String::class.java, String::class.java))

    /**
     * Check if the last response in the Requester matches the expected values
     */
    fun match(expected: Map<String, String>, fieldPrefix: String? = null) {
        val lastResponse = requester.lastResponse

        val assertions = expected
                .onEach {
                    Assertions.assertTrue(matchers.containsKey(it.key), """No matcher registered for property "${it.key}"""")
                }
                .mapKeys { matchers[it.key]!! }
                .toList()
                .map { (field, expected) ->
                    val realFieldPath = when (fieldPrefix) {
                        null -> field.fieldPath
                        else -> fieldPrefix + field.fieldPath
                    }

                    val value = try {
                        lastResponse.context.getValue(realFieldPath)
                    } catch (e: JXPathNotFoundException) {
                        null
                    }
                    val realValue = field.actualConversion(value)
                    val realExpected = field.expectedConversion(expected)

                    Executable { field.comparison(realExpected, realValue) }
                }

        Assertions.assertAll(assertions)
    }
}

/**
 * Response Matcher for matching a set of values that represent a list of entries
 */
class ListResponseMatcher(
        private val responseMatcher: ResponseMatcher,
        private val preIndexPath: String,
        private val postIndexPath: String
) {
    /**
     * Match every entry in the given list of maps using our response matcher
     */
    fun match(expectedMaps: List<Map<String, String>>) {
        val expectations =expectedMaps.mapIndexed { index, map ->
            val fieldPathPrefix = "$preIndexPath${index + 1}$postIndexPath"
            Executable { responseMatcher.match(map, fieldPathPrefix) }
        }

        Assertions.assertAll(expectations)
    }

}
