package com.nisus.baotool.util.collection.skmap

import io.github.abandno.baotool.test.asset.User
import io.github.abandno.baotool.util.collection.skmap.SoMap

/**
 * ${description}
 * @author nisus
 * @version 0.1
 * @since 2023/4/20 18:03
 */
class StringKeyMapTest extends groovy.util.GroovyTestCase {
    void test1() {
        SoMap m = SoMap.of(
                "a", 1, "b",
                5.666,
                "c", true);

        m.puts("user", new User("小花", 19), "x", 89);
        System.out.println(m);

        System.out.println(m.getBoolean("c"));
        User user = m.getValue("user", User.class, null);
        System.out.println(user);

        // ---
        // {a=1, b=5.666, c=true, x=89, user=User(name=小花, age=19)}
        // true
        // User(name=小花, age=19)
    }
}
