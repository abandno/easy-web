package com.nisus.baotool.util.lang

import io.github.abandno.baotool.test.asset.ExampleEnum
import io.github.abandno.baotool.test.asset.ExampleIEnum
import io.github.abandno.baotool.util.lang.EnumUtil
import spock.lang.Specification
import spock.lang.Unroll

class EnumUtilTest extends Specification {

    @Unroll
    def "from Value where enumClass=#enumClass and value=#value then expect: #expectedResult"() {
        expect:
        EnumUtil.fromValue(enumClass, value).orElse(null) == expectedResult

        where:
        enumClass          | value || expectedResult
        ExampleEnum.class  | "MT"  || ExampleEnum.MT        // name() 定位
        ExampleIEnum.class | 2     || ExampleIEnum.ORDERED  // getValue() 定位
        ExampleIEnum.class | "2"   || ExampleIEnum.ORDERED  // getValue() 定位, 类型不敏感
    }

    @Unroll
    def "from Field where fieldName=#fieldName and enumClass=#enumClass and value=#value then expect: #expectedResult"() {
        expect:
        EnumUtil.fromField(enumClass, fieldName, value).orElse(null) == expectedResult

        where:
        fieldName | enumClass          | value || expectedResult
        "code"    | ExampleEnum.class  | 1     || ExampleEnum.MT
        "value"   | ExampleIEnum.class | 1     || ExampleIEnum.INIT
        "value"   | ExampleIEnum.class | "1"   || null
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme