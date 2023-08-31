package com.nisus.baotool.core.enums

import io.github.abandno.baotool.core.enums.IEnum;

enum AIEnum implements IEnum<Integer> {
    BIG(1, "大杯"),
    MIDDLE(2, "中杯"),
    SMALL(3, "小杯子"),
    ;
    private final Integer code;
    private final String desc;

    AIEnum(Integer code, String desc) {
        this.code = code
        this.desc = desc
    }

    @Override
    Integer getValue() {
        return this.code;
    }

    @Override
    String getLabel() {
        return this.desc;
    }
}