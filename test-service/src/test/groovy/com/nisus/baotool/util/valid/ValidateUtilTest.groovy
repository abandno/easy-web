package com.nisus.baotool.util.valid

import io.github.abandno.baotool.test.asset.User
import io.github.abandno.baotool.util.valid.ValidateUtil

/**
 * ${description}
 * @author nisus
 * @version 0.1
 * @since 2023/4/20 20:31
 */
class ValidateUtilTest extends groovy.util.GroovyTestCase {

    void test1() {
        User user = new User(null, 10)
        println ValidateUtil.validateQuietly(user)
        User user1 = new User("", 1)
        println ValidateUtil.validate(user1)

        // ---
        // false
        // com.nisus.baotool.core.exception.ArgumentException@2ca26d77[message='User.name' 不能为空
        // ,errors=['User.name' 不能为空]]
        // 	at com.nisus.baotool.util.valid.ValidateUtil.validate(ValidateUtil.java:72)
        //  ...
    }

}
