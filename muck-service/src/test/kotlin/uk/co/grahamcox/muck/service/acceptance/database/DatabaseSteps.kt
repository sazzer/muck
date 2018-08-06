package uk.co.grahamcox.muck.service.acceptance.database

import cucumber.api.java.Before

/**
 * Clean out the database before each scenario
 */
class DatabaseSteps(private val databaseCleaner: DatabaseCleaner) {
    @Before
    fun cleanDatabase() {
        databaseCleaner.clean()
    }
}
