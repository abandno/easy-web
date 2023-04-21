package com.nisus.baotool.util.collection

import com.nisus.baotool.test.asset.User

/**
 * ${description}
 * @author nisus
 * @version 0.1
 * @since 2023/4/20 18:22
 */
class CollDiffTest extends groovy.util.GroovyTestCase {
    void test1() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4)
        List<Integer> list2 = Arrays.asList(1, 2, 99, 100)
        CollDiff<Integer> collDiff = CollDiff.of(list1, list2)
        println collDiff.getOnlyCollection1()
        println collDiff.getOnlyCollection2()
        println collDiff.getIntersection()

        // ---
        // [3, 4]
        // [99, 100]
        // [1, 2]
    }

    void "test 指定比较的key"() {
        User u1 = new User("1", 1)
        User u2 = new User("2", 1)
        User u3 = new User("3", 1)
        User u4 = new User("4", 1)
        User u5 = new User("5", 1)

        List<User> list1 = Arrays.asList(u1, u2)
        List<User> list2 = Arrays.asList(u5, u4, u3, u2)

        // name 一样, 认为是同一个元素
        CollDiff<User> cd = CollDiff.of(list1, list2, { it -> it.getName() })
        println cd.getIntersection() // [User(name=2, age=1)]
    }

}
