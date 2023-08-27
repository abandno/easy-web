package com.nisus.baotool.util.etc;

import org.junit.jupiter.api.Test;


public class SoutTest {

    @Test
    public void demo() {
        String a = "aaa";
        int b = 123;
        Boolean c = true;

        // 没有 Sout 之前的打印
        System.out.println("a=" + a + "," + "b=" + b + ", c=" + c);
        // 有了 Sout 之后
        Sout.println("a={}, b={}, c={}", a, b, c);

        // 数组打印
        String[] arr = new String[]{"a", "b", "c"};
        System.out.println("我是数组: " + arr); // [Ljava.lang.String;@4450d156
        Sout.println("我是数组: ", arr); // a b c

        // System.out.println(a, b, c); // 不支持
        Sout.println(a, b, c);
    }


}