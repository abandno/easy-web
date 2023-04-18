package com.nisus.baotool.core.exception


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
    def "set Code where code=#code then expect: #expectedResult"() {
        expect:
        bRuntimeException.setCode(code) == expectedResult

        where:
        code || expectedResult
        0    || new BRuntimeException(null, "msg", "args")
    }

    @Unroll
    def "set Errors where errors=#errors then expect: #expectedResult"() {
        expect:
        bRuntimeException.setErrors(errors) == expectedResult

        where:
        errors     || expectedResult
        ["String"] || new BRuntimeException(null, "msg", "args")
    }

    @Unroll
    def "append Errors where errors=#errors then expect: #expectedResult"() {
        expect:
        bRuntimeException.appendErrors(errors) == expectedResult

        where:
        errors   || expectedResult
        "errors" || new BRuntimeException(null, "msg", "args")
    }

    @Unroll
    def "append Errors 2 where errors=#errors then expect: #expectedResult"() {
        expect:
        bRuntimeException.appendErrors(errors) == expectedResult

        where:
        errors     || expectedResult
        ["String"] || new BRuntimeException(null, "msg", "args")
    }

    @Unroll
    def "append Error where template=#template and params=#params then expect: #expectedResult"() {
        expect:
        bRuntimeException.appendError(template, params) == expectedResult

        where:
        template   | params   || expectedResult
        "template" | "params" || new BRuntimeException(null, "msg", "args")
    }

    @Unroll
    def "to String"() {
        expect:
        bRuntimeException.toString() == expectedResult

        where:
        expectedResult << "expectedResult"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme