package com.nisus.baotool.test


import com.nisus.baotool.core.ext.lang.Tuple3
import org.junit.jupiter.api.Test

class FooGroovyTest {

    @Test
    void foo() {


    }

    @Test
    void fun() {
        println m()
    }

    Tuple3<String, Integer, List> m() {
        return new Tuple3<>("小花", 18, ["A", "C", "D"] as List)
    }

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
