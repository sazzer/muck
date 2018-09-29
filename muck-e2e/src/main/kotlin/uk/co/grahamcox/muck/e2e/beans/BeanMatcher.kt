package uk.co.grahamcox.muck.e2e.beans

import org.junit.Assert
import kotlin.reflect.KProperty1

/**
 * Matcher to compare the given bean to the expected input values
 */
class BeanMatcher<T>(
        val fields: Map<String, BeanField<T>>
) {
    /** Details of a single field to match */
    data class BeanField<T>(
            val getter: KProperty1<T, *>
    )

    /**
     * Match the given bean to the expected input values
     * @param bean The bean to compare
     * @param input The input values to compare with
     */
    fun match(bean: T, input: Map<String, String>) {
        val unexpected = input.keys.filterNot(fields::containsKey)
        if (unexpected.isNotEmpty()) {
            throw IllegalArgumentException("Unexpected input keys: $unexpected")
        }

        input.entries.map { it.key to it.value }
                .map { fields[it.first]!! to it.second }
                .forEach { (beanField, expectedValue) ->
                    val value = beanField.getter.get(bean)
                    Assert.assertEquals(expectedValue, value)
                }
    }
}
