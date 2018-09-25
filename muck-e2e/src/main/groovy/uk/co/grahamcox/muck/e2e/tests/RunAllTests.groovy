package uk.co.grahamcox.muck.e2e.tests

import io.github.classgraph.ClassGraph
import org.junit.internal.TextListener
import org.junit.runner.JUnitCore
import org.junit.runner.Request

/**
 * Wrapper to run all of the tests
 */
class RunAllTests {
    /**
     * Actually run all of the tests
     * @param args command line arguments. Ignored.
     */
    static void main(String... args) {

        def testClasses = new ClassGraph()
                .whitelistPackages(RunAllTests.class.packageName)
                .scan()
                .allClasses
                .filter { it.name.endsWith("Spec") }
                .loadClasses()


        def runner = new JUnitCore()
        runner.addListener(new TextListener(System.err))

        def testRequest = Request.classes(*testClasses.toArray())

        def result = runner.run(testRequest)
        System.exit(result.wasSuccessful() ? 0 : 1)
    }
}
