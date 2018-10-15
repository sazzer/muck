package uk.co.grahamcox.muck.service.database

import uk.co.grahamcox.muck.service.model.Page


/**
 * Make appropriate database calls to get an entire page of records
 * This will request one record more than is needed, and then if we actually get that additional
 * record is the only time we need to go and get the total count of records - if we get less than the
 * page + 1 then we know we've reached the end of the resultset
 */
fun <T> queryPage(offset: Int, pageSize: Int, callback: PageCallback<T>) : Page<T> {
    val pageData = if (pageSize > 0) {
        callback.getPage(offset.toLong(), pageSize + 1L)
    } else {
        emptyList()
    }

    // We need to get the actual count of records from the database in three cases:
    // * No records were requested - we've got nothing to base our calculation on
    // * No records were retrieved and we're not at the start of the resultset  - we're probably off the end of the list
    // * We retrieved our (pageSize + 1), meaning that there's more than pageSize but we don't know how many more
    val totalSize = when {
        pageSize == 0 -> callback.getTotal()
        pageData.isEmpty() && offset > 0 -> callback.getTotal()
        pageData.size > pageSize -> callback.getTotal()
        else -> offset + pageData.size
    }

    val realData = if (pageData.size > pageSize) {
        pageData.subList(0, pageSize.toInt())
    } else {
        pageData
    }
    return Page(realData, offset, totalSize)
}
