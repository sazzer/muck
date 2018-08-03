package uk.co.grahamcox.muck.service.acceptance

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.Ignore
import org.junit.runner.RunWith

/**
 * Runner to run all of the acceptance tests
 */
@RunWith(Cucumber::class)
@CucumberOptions(tags = ["not @wip", "not @ignore", "not @manual"],
        plugin = ["pretty", "html:target/site/cucumber/cucumber"],
        strict = true)
class AllIT

/**
 * Runner to run all of the work-in-progress acceptance tests
 */
@RunWith(Cucumber::class)
@CucumberOptions(tags = ["@wip", "not @ignore", "not @manual"],
        plugin = ["pretty", "html:target/site/cucumber/cucumber"],
        strict = false)
class WipIT
