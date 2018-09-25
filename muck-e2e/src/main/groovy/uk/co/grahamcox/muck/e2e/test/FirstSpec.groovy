package uk.co.grahamcox.muck.e2e.test

import spock.lang.Specification

class FirstSpec extends Specification {
    def "one plus one should equal two"() {
        expect:
        1 + 1 == 2
    }
}
