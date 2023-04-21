package com.nisus.baotool.test.controller;

import com.nisus.baotool.experiment.Sout;
import com.nisus.baotool.test.asset.ExampleEnum;
import com.nisus.baotool.test.asset.ExampleIEnum;
import com.nisus.baotool.test.asset.TestSeBean;
import com.nisus.baotool.test.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 *
 * @author L&J
 * @date 2021/10/1 11:56 上午
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    private TestService testService;
    ExecutorService executorService = Executors.newFixedThreadPool(1);

    // // @CatInvoke(arguments = "#a", returns = "${'====>' + '#controller#' + #root.returns}")
    // @RequestMapping("/catAop")
    // public SoMap testCat(String a, Integer b, Double c) {
    //     // System.out.println("^^^^^^^^^^ cat aop");
    //     //
    //     // return SoMap.of("a", a, "b", b, "c", c);
    //     // testService.testCat(a, b, c);
    //
    //     Runnable runnable = () -> {
    //         // System.out.println(LogTraceAspect.howStackContextHolder.get());
    //         testService.testCat1(a, b, c);
    //     };
    //     executorService.submit(runnable);
    //     // new Thread(runnable).start();
    //     // runnable.run();
    //
    //     return SoMap.of();
    // }


    @RequestMapping("/testSe")
    public TestSeBean testSe(@RequestBody TestSeBean params) {
        System.out.println(params);
        // TestSeBean testSeBean = new TestSeBean();
        // testSeBean.setPoolType(PoolTypeEnum.CUSTOM);
        // return testSeBean;
        // params.setExampleIEnumCode(2);

        TestSeBean res = new TestSeBean();
        res.setExampleIEnumCode(2);
        res.setNum5(3.1415926);
        return res;
    }

    @GetMapping("/testSeGet1")
    public String testSeGet1(String e1, @RequestParam ExampleIEnum e2, @RequestParam ExampleEnum e3) {
        Sout.println(e1, e2, e3);
        return "OOOOOK";
    }

    @GetMapping("/testSeGet2")
    public TestSeBean testSeGet2(TestSeBean params) {
        Sout.println(params);
        return params;
    }

    // @RequestMapping("/testSubSe")
    // public TestSeBean testSubSe(SubTestSeBean params) {
    //     System.out.println(params);
    //     // TestSeBean testSeBean = new TestSeBean();
    //     // testSeBean.setPoolType(PoolTypeEnum.CUSTOM);
    //     // return testSeBean;
    //     params.setExampleIEnumCode(2);
    //     return params;
    // }
}
