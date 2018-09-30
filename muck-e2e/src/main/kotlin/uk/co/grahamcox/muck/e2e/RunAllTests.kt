package uk.co.grahamcox.muck.e2e

import cucumber.api.cli.Main
import org.slf4j.LoggerFactory

/** Logger to use */
private val LOG = LoggerFactory.getLogger("RunAllTests")

/**
 * Helper to run all of the correct cucumber tests
 */
fun main(args: Array<String>) {
    LOG.debug("Starting tests")
    val exitStatus = Main.run(arrayOf(
            "-t", "not @wip",
            "-t", "not @ignore",
            "-t", "not @manual",
            "-g", "classpath:uk/co/grahamcox/muck/e2e",
            "classpath:uk/co/grahamcox/muck/e2e"
    ), Thread.currentThread().contextClassLoader)

    LOG.debug("Finished all tests. Exit status: {}", exitStatus)
    System.exit(exitStatus.toInt())
}
