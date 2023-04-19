package com.nisus.baotool.test

import com.nisus.baotool.core.ext.lang.Tuple2
import com.nisus.baotool.core.ext.lang.Tuple3
import org.junit.jupiter.api.Test

class FooGroovyTest {

    @Test
    def void foo() {
        def tup2 = new Tuple2<Integer, String>(18, "小花")
        println tup2
        println tup2.getFirst()
        println tup2.getSecond()

        tup2.setFirst(28)
        println tup2.getFirst()
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
