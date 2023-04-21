package com.nisus.baotool.test.asset;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 *
 * @author L&J
 * @date 2021/10/1 6:34 下午
 */
@Getter
@AllArgsConstructor
public enum ExampleEnum {

    MT(1, 1, "美团"),
    TX(2, 2, "腾讯"),
    AL(3, 3, "阿里"),
    ZJ(4, 4, "字节"),
    ;

    private final Integer code;
    private final Integer value;
    private final String desc;


}
