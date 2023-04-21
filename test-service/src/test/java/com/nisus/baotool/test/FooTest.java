package com.nisus.baotool.test;

import com.nisus.baotool.test.asset.User;
import com.nisus.baotool.util.reflect.FieldNameUtil;
import org.junit.jupiter.api.Test;

/**
 * @author dafei
 * @version 0.1
 * @date 2023/4/18 23:08
 */
public class FooTest {

    @Test
    public void foo() {
        System.out.println(FieldNameUtil.get(User::isGoodMan));     // goodMan
        System.out.println(FieldNameUtil.lc2lu(User::isGoodMan));   // good_man
    }



}
