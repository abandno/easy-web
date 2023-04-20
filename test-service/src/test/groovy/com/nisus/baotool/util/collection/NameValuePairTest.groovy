package com.nisus.baotool.util.collection

/**
 * ${description}
 * @author nisus
 * @version 0.1
 * @since 2023/4/20 18:05
 */
class NameValuePairTest extends groovy.util.GroovyTestCase {
    void test1() {
        NameValuePair nameValuePair = NameValuePair.from("name=李斯&age=199&isLive=false")
        nameValuePair.forEach({ k, v ->
            println "$k ----- $v"
        })

        nameValuePair.put("p1", "eeee")
        nameValuePair.put("p2", null)
        println nameValuePair.toString()
        println nameValuePair.containsKey("p2")

        // ---
        // name ----- 李斯
        // age ----- 199
        // isLive ----- false
        // name=李斯&age=199&isLive=false&p1=eeee&p2=
        // true
    }

    void "test 字典序"() {
        // 默认按添加的顺序, 传入 true, 则按字典序
        NameValuePair pair = new NameValuePair(true)
        pair.puts(
                "z", "zzz",
                "b", "bbb",
                "a", "aaaa",
                "c", "ccc")
        println pair.toString() // a=aaaa&b=bbb&c=ccc&z=zzz
    }
}
