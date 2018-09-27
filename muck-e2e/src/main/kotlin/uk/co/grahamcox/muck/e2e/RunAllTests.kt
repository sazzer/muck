package uk.co.grahamcox.muck.e2e

import cucumber.api.cli.Main

/**
 * Helper to run all of the correct cucumber tests
 */
fun main(args: Array<String>) {
    Main.main(arrayOf(
            "-t", "not @wip",
            "-t", "not @ignore",
            "-t", "not @manual",
            "-g", "classpath:uk/co/grahamcox/muck/e2e",
            "classpath:uk/co/grahamcox/muck/e2e"
    ))
}
