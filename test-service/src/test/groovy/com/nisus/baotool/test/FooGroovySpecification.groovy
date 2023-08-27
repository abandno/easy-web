package com.nisus.baotool.test

import spock.lang.Specification
import spock.lang.Unroll

class FooGroovySpecification extends Specification {
    @Unroll
    def "a+b=c, #a + #b ?= #c"() {
        expect:
        a + b == c
        where:
        a | b || c
        3 | 5 || 8
        1 | 3 || 6
        7 | 6 || 13
    }
}
