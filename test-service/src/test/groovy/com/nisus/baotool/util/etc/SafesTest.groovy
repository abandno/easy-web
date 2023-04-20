package com.nisus.baotool.util.etc

import com.nisus.baotool.asset.User

/**
 * ${description}
 * @author nisus
 * @version 0.1
 * @since 2023/4/20 18:34
 */
class SafesTest extends groovy.util.GroovyTestCase {

    void test1() {
        String str = null
        println Safes.of(str as String).length() // 0
    }

    void test2() {
        User user = null;
        // user.getName() 必然报错
        println Safes.getQuietly({ -> user.getName() }, "none name") // 内部 catch 掉了异常
        println Safes.get(user, { u -> u.getName() }, "none name")

        user = new User(null, 1)
        // user.getName().length() 必然报错
        println Safes.getQuietly({ -> user.getName().length() }, 9999) // 内部 catch 掉了异常
        println Safes.get(user, { u -> u.getName() }, { n -> n.length() }, "none name")
    }

}
