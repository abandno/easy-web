package com.nisus.baotool.core.enums

import io.github.abandno.baotool.core.enums.IEnum
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 *
 * @author nisus
 * @version 0.1
 * @since 2023/4/19 11:14
 */
class IEnumTest extends Specification {
    @Shared
    IEnum iEnum = AIEnum.MIDDLE

    @Unroll
    def "demo"() {
        when:
        println "---"
        then:
        println iEnum;
        println iEnum.prettyString()
        println "${iEnum.getValue()} +++++ ${iEnum.getLabel()}"

        // ---
        // MIDDLE
        // AIEnum.MIDDLE(2,中杯)
        // 2 +++++ 中杯
    }

    @Unroll
    def "相等比较 expect: #b"() {
        when:
        def current = AIEnum.SMALL
        then:
        current.eq(b)

        where:
        a     | b
        iEnum | AIEnum.SMALL
        iEnum | 3
        iEnum | "3"
    }


}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme