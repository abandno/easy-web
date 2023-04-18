package com.nisus.baotool.test;

import com.nisus.baotool.core.exception.BRuntimeException;
import org.junit.jupiter.api.Test;

/**
 * @author dafei
 * @version 0.1
 * @date 2023/4/18 23:08
 */
public class FooTest {

    @Test
    public void foo() {
        BRuntimeException ex = new BRuntimeException(new NullPointerException("NPE"), "ljjjgjgj {}", 999897);
        // System.out.println(ex);
        ex.printStackTrace();
    }



}
