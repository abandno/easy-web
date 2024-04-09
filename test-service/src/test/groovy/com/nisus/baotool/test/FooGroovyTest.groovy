package com.nisus.baotool.test


import io.github.abandno.baotool.core.tuple.Tuple3
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
        //准备测试上下文，一些变量啥的
        given:
        def x = 1
        def y = 2
        //执行一些计算逻辑
        when:
        def z = x + y
        //后续操作
        then:
        //对计算结果断言
        assert z == 3

    }

}
