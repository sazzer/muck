package uk.co.grahamcox.muck.e2e.beans

import kotlin.reflect.KMutableProperty1

/**
 * Populator to update the given bean to the expected input values
 */
class BeanPopulator<T>(
        val fields: Map<String, BeanField<T, *>>
) {
    /** Details of a single field to match */
    data class BeanField<T, P>(
            val setter: KMutableProperty1<T, P>
    )

    /**
     * Populate the given bean with the input values
     * @param bean The bean to update
     * @param input The input values to update with
     */
    fun populate(bean: T, input: Map<String, String>) {
        val unexpected = input.keys.filterNot(fields::containsKey)
        if (unexpected.isNotEmpty()) {
            throw IllegalArgumentException("Unexpected input keys: $unexpected")
        }

        input.entries.map { it.key to it.value }
                .map { fields[it.first]!! to it.second }
                .forEach { (beanField, newValue) ->
                    val setter = (beanField.setter as KMutableProperty1<T, Any?>)
                    setter.set(bean, newValue)
                }
    }
}
