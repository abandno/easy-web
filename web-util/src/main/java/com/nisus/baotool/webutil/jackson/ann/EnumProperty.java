package com.nisus.baotool.webutil.jackson.ann;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nisus.baotool.webutil.jackson.se.EnumPropertySerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 反序列化:
 * 1. 字段类型是 code. labelProperty 非空时, 增加 labelProperty 字段, code 字段原值; 否则, code 字段放 label.
 * 2. 字段类型是 Enum. labelProperty 非空时, 增加 labelProperty 字段, code 字段原值; 否则, code 字段放 label.
 *
 * @author L&J
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@JacksonAnnotationsInside
@JsonSerialize(using = EnumPropertySerializer.class)
public @interface EnumProperty {

    /**
     * 枚举类
     */
    Class<? extends Enum<?>> value();

    /**
     * label 字段名
     *
     * 默认, 原字段放枚举 label; 非空时, 原字段放原始值, 此配置字段名放 label.
     */
    String labelProperty() default "";

}
