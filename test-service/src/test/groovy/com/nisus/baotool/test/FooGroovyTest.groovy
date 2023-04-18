package com.nisus.baotool.test

class FooGroovyTest {

    static void main(String[] args) {
        given:
        def x = 1
        def y = 2
        when:
        def z = x + y
        then:
        assert z == 3

    }

}
