package com.nisus.baotool.util.etc

import com.nisus.baotool.test.asset.User

/**
 * ${description}
 * @author nisus
 * @version 0.1
 * @since 2023/4/20 19:11
 */
class JsonFilterUtilTest extends groovy.util.GroovyTestCase {
    void test1() {
        User data = new User("123456789", 1)

        JsonFilterUtil.FilterConfig config = new JsonFilterUtil.FilterConfig()
        config.stringMaxLen = 3
        println JsonFilterUtil.toJSONString(data, config)

        // ---
        // 19:44:15.690 [main] INFO com.nisus.baotool.util.etc.JsonFilterUtil - 截取超长字段: name, type: String, length: 9
        // {"age":1,"name":"123<...9>"}
    }
}
