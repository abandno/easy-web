package com.nisus.baotool.test;

import cn.hutool.core.util.NumberUtil;
import org.junit.jupiter.api.Test;

/**
 * @author dafei
 * @version 0.1
 * @date 2023/4/18 23:08
 */
public class FooTest {

    @Test
    public void foo() {
        System.out.println(NumberUtil.sub(0.3, 0.2));
        System.out.println(NumberUtil.sub(0.3, 0.2) == 0.1);
    }



}
