package uk.co.grahamcox.muck.service.database

/**
 * Callback for querying for a page of results.
 */
interface PageCallback<T> {
    /**
     * Get the page of records as defined by the provided parameters
     * @param offset The offset of the first record on the page
     * @param count The count of records to get
     * @return the matching records
     */
    fun getPage(offset: Long, count: Long): List<T>

    /**
     * Get the total number of records
     * @return the total number of records
     */
    fun getTotal(): Int
}
