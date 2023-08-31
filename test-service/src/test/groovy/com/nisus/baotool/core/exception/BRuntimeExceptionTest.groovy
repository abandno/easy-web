package com.nisus.baotool.core.exception

import io.github.abandno.baotool.core.exception.BRuntimeException
import io.github.abandno.baotool.core.exception.SignalException
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import spock.lang.Specification
import spock.lang.Unroll

class BRuntimeExceptionTest extends Specification {
    @Mock
    List<String> errors
    @Mock
    Object backtrace
    @Mock
    Throwable cause
    //Field stackTrace of type StackTraceElement - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    List<Throwable> SUPPRESSED_SENTINEL
    @Mock
    List<Throwable> suppressedExceptions
    @InjectMocks
    BRuntimeException bRuntimeException

    def setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Unroll
    def "demo 构造"() {
        // when:
        // def ex = new RuntimeException("xxx")
        // then:
        // println new BRuntimeException("msggg")
        // println "hhh"
        // println ex

        expect:
        println ex
        if (x) {
            ex.printStackTrace()
        }

        where:
        x     | ex
        // 无参构造
        false | new BRuntimeException()
        // 单 message
        false | new BRuntimeException("msggggggg")
        // 带模板
        false | new BRuntimeException("{}, 你好, 还你 {}元", "余欢水", 12345.321)
        // 嵌套异常
        true  | new BRuntimeException(new NullPointerException("NNNNPE!!!"))
        // 嵌套异常 + message, 放到第一个入参, 就是怕你忘了哦, 丢失堆栈, 你怎么定位问题?
        true  | new BRuntimeException(new NullPointerException("NNNNPE!!!"), "今天收到的 {} 个空指针咯. 第一个入参是被包的异常, 这么显眼, 你还遗漏不能怪我咯!", 3)
        // 嵌套异常放最后个模板参数
        true  | new BRuntimeException("被包异常在最后一个模板参数, 放心, 不受模板占位符影响, 放上了一定会被嵌套. {} {} {}", 1, 2, new IllegalStateException("嵌套异常"))
    }

    @Unroll
    def "SignalException 无堆栈信息"() {
        when:
        def ex = new SignalException("我只是一个信号异常, 不要指望我输出堆栈信息")
        then:
        println ex
    }

    // @Unroll
    // def "set Errors where errors=#errors then expect: #expectedResult"() {
    //     expect:
    //     bRuntimeException.setErrors(errors) == expectedResult
    //
    //     where:
    //     errors     || expectedResult
    //     ["String"] || new BRuntimeException(null, "msg", "args")
    // }
    //
    // @Unroll
    // def "append Errors where errors=#errors then expect: #expectedResult"() {
    //     expect:
    //     bRuntimeException.appendErrors(errors) == expectedResult
    //
    //     where:
    //     errors   || expectedResult
    //     "errors" || new BRuntimeException(null, "msg", "args")
    // }
    //
    // @Unroll
    // def "append Errors 2 where errors=#errors then expect: #expectedResult"() {
    //     expect:
    //     bRuntimeException.appendErrors(errors) == expectedResult
    //
    //     where:
    //     errors     || expectedResult
    //     ["String"] || new BRuntimeException(null, "msg", "args")
    // }
    //
    // @Unroll
    // def "append Error where template=#template and params=#params then expect: #expectedResult"() {
    //     expect:
    //     bRuntimeException.appendError(template, params) == expectedResult
    //
    //     where:
    //     template   | params   || expectedResult
    //     "template" | "params" || new BRuntimeException(null, "msg", "args")
    // }
    //
    // @Unroll
    // def "to String"() {
    //     expect:
    //     bRuntimeException.toString() == expectedResult
    //
    //     where:
    //     expectedResult << "expectedResult"
    // }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme