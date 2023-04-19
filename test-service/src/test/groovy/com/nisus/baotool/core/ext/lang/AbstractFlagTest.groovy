package com.nisus.baotool.core.ext.lang

/**
 * ${description}
 * @author nisus
 * @version 0.1
 * @since 2023/4/19 12:52
 */
class AbstractFlagTest extends GroovyTestCase {

    // @Test
    void test() {
        // 初始化
        ColorFlag flag = new ColorFlag(ColorFlag.RED | ColorFlag.BLUE);
        System.out.println(flag); // RED|BLUE
        println "友好显示: ${flag}, 实际值是: ${flag.getState()}"
        System.out.println(flag.has(ColorFlag.RED)); // true

        // 新增 flag
        flag.add(ColorFlag.GREEN | ColorFlag.ORANGE);
        assertFalse(flag.has(ColorFlag.VIOLET));
        assertTrue(flag.has(ColorFlag.ORANGE));
        assertTrue(flag.has(ColorFlag.RED)); // 红色 还在的
        assertTrue(flag.isGreen());

        // ---
        // RED|BLUE
        // 友好显示: RED|BLUE, 实际值是: 33
        // true
    }

}
