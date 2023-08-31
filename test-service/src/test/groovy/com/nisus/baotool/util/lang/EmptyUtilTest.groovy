package com.nisus.baotool.util.lang

import io.github.abandno.baotool.util.lang.EmptyUtil
import org.mockito.MockitoAnnotations
import org.omg.CORBA.Object
import spock.lang.Specification
import spock.lang.Unroll

class EmptyUtilTest extends Specification {

    static Object[] args = new Object[0]

    def setup() {
        MockitoAnnotations.initMocks(this)
    }


    @Unroll
    def "is Empty where subject=#subject and otherEmptyCases=#otherEmptyCases then expect: #expectedResult"() {
        given:
        // when(defaultEmptyMode.isEmpty(any(), anyVararg())).thenReturn(true)

        expect:
        EmptyUtil.EXTEND_MODE.isEmpty(subject, otherEmptyCases) == expectedResult

        // subject == expectedResult
        // args 是 Object[0]
        where:
        subject       | otherEmptyCases || expectedResult
        null          | args            || true
        ""            | args            || true
        "  \n \t "    | args            || true
        "null"        | args            || true
        "NULL"        | args            || false
        0             | args            || true
        0L            | args            || true
        0.000         | args            || true
        []            | args            || true
        new HashMap() | args            || true
        9999          | args            || false
        9999          | 9999            || true
    }

    @Unroll
    def "JdkEmptyMode: where subject=#subject and otherEmptyCases=#otherEmptyCases then expect: #expectedResult"() {
        given:
        // when(defaultEmptyMode.isEmpty(any(), anyVararg())).thenReturn(true)

        expect:
        EmptyUtil.JDK_MODE.isEmpty(subject, otherEmptyCases) == expectedResult

        where:
        subject       | otherEmptyCases || expectedResult
        null          | args            || true
        ""            | args            || true
        "  \n \t "    | args            || true
        "null"        | args            || false
        "NULL"        | args            || false
        0             | args            || false
        0L            | args            || false
        0.000         | args            || false
        []            | args            || true
        new HashMap() | args            || true
        9999          | args            || false
        9999          | 9999            || true
    }

    @Unroll
    def "defaultIfEmpty: where subject=#subject, defValue=#defValue"() {
        given:
        expect:
        EmptyUtil.JDK_MODE.defaultIfEmpty(subject, defValue, otherEmptyCases) == expectedResult

        where:
        subject       | defValue | otherEmptyCases || expectedResult
        null          | 111      | args            || 111
        ""            | 111      | args            || 111
        "  \n \t "    | 111      | args            || 111
        "null"        | 111      | args            || "null"
        "NULL"        | 111      | args            || "NULL"
        0             | 111      | args            || 0
        0L            | 111      | args            || 0L
        0.000         | 111      | args            || 0.000
        []            | 111      | args            || 111
        new HashMap() | 111      | args            || 111
        9999          | 111      | args            || 9999
        9999          | 111      | 9999            || 111
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme