package uk.co.grahamcox.muck.e2e.spring

import cucumber.api.java.Before
import org.springframework.boot.test.context.SpringBootTest

/**
 * Cucumber steps for setting up Spring
 */
@SpringBootTest(classes = [E2eConfig::class])
class SpringSteps {
    /**
     * No-op function so that there is a Cucumber annotated method in this class
     */
    @Before
    fun startSpring() {
    }
}
