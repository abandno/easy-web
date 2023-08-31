package com.nisus.baotool.core.ext.math

import cn.hutool.core.util.NumberUtil
import io.github.abandno.baotool.core.ext.math.Numbor

/**
 * ${description}
 * @author nisus
 * @version 0.1
 * @since 2023/4/19 13:37
 */
public class NumborTest extends GroovyTestCase {

    void test缘由() {
        println 0.3 - 0.2
        println 0.3 - 0.2 == 0.1
        println(NumberUtil.sub(0.3, 0.2));
        println(NumberUtil.sub(0.3, 0.2) == 0.1);

        // NumberUtil.div(1, null); // NullPointerException
        println NumberUtil.mul(1, null); // 应该等于多少? 1 or 0 ? 还是 null? => 0
        // NumberUtil.div(1, 0); // ArithmeticException
    }

    void test1() {
        // ((10 + 2) / 3 - 2) * 3.5
        Numbor r = new Numbor(10).add(2).div(3).sub(2).mul(3.5);
        System.out.println("r = " + r);
        // ---
        // r = 7.00000000000

        r.ifValid {
            println "得到有效的结果: ${it}"
        }
    }

    void test2() {
        // 3 / (4 + 5)
        def a = new Numbor(4).add(5)
        def r = new Numbor(3).mul(a)
        println r // 27
    }

    void "test 自定义规则"() {
        // PS: 使用 AbstractFlag 配置方式
        def customRule = Numbor.rule(Numbor.Rule.IGNORE_NULL | Numbor.Rule.INFINITY_AS_0 | Numbor.Rule.IGNORE_NAN)
        // 或 def customRule = new Numbor.Rule(Numbor.Rule.IGNORE_NULL | Numbor.Rule.INFINITY_AS_0 | Numbor.Rule.IGNORE_NAN)
        println customRule

        // 应用自定义规则
        println customRule.apply(null).add(0).div(0).mul(10).div(0) // 0
    }

    void "test `忽略规则` 优先级高于 `AS 规则`"() {
        // 自定义规则, null 值忽略, null 当作 0
        def customRule = new Numbor.Rule(Numbor.Rule.IGNORE_NULL | Numbor.Rule.NULL_AS_0)
        def r = customRule.apply(1).add(null as Integer).mul(null as Integer)
        // 1 ? 0 ? 由于忽略规则优先级高, null 都被忽略, 所有输出 1
        println r // 1
    }


}
