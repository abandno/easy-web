package com.nisus.baotool.experiment.bean

import io.github.abandno.baotool.experiment.bean.Result

/**
 * @author nisus
 * @version 0.1
 * @since 2023/4/20 11:56
 */
public class ResultTest extends GroovyTestCase {

    void testOoo() {
        // service 层返回结果
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        Result<List<Integer>> r = Result.ok(list)
        // 返回给请求方
        System.out.println(r)
    }

}
