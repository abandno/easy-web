package com.nisus.baotool.tuple

import io.github.abandno.baotool.core.tuple.Tuple2
import io.github.abandno.baotool.core.tuple.Tuples
import spock.lang.Specification
import spock.lang.Unroll

class TuplesTest extends Specification {
    @Unroll
    def "a+b=c, #a + #b ?= #c"() {
        expect:
        Tuples.of(a, b) == new Tuple2<>(a, b)
        where:
        a | b || c
        3 | 5 || 8
        1 | 3 || 4
        7 | 6 || 13
    }


    def "给定位置类型数组, 转为带类型提示的元组"() {
        when:
        def arr = ["str", 1, 2.0, true].toArray()
        def apply = Tuples.<String, Integer, Double, Boolean> fn4().apply(arr)
        then:
        println apply.getT1().length();
        println apply.getT4() == true
    }
}
