package com.nisus.baotool.webutil.jackson.ann;


import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nisus.baotool.webutil.jackson.se.NumberPropertySerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.RoundingMode;

/**
 * Jackson 数值字段
 * @since 2022-01-21 13:48:04
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@JacksonAnnotationsInside
@JsonSerialize(using = NumberPropertySerializer.class)
public @interface NumberProperty {

    /**保留的小数位. 浮点数有效. 默认不 round*/
    int scale() default -1;
    /**round mode. 浮点数有效*/
    RoundingMode roundingMode() default RoundingMode.HALF_UP;

    /**
     * 规则参见: {@link NumberUtil#decimalFormat}.
     * 默认原样输出.
     * 不为空时, 将输出为 String.
     * 优先级高于 scale.
     */
    String pattern() default "";
}
