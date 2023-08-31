package io.github.abandno.baotool.webutil.jackson.ann;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.abandno.baotool.webutil.jackson.de.EmptyToNullPropertyDeserializer;
import io.github.abandno.baotool.webutil.jackson.se.EmptyToNullPropertySerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * empty to null 字段
 * @since 2022-02-22 17:43:33
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@JacksonAnnotationsInside
@JsonSerialize(using = EmptyToNullPropertySerializer.class)
@JsonDeserialize(using = EmptyToNullPropertyDeserializer.class)
public @interface EmptyToNullProperty {

    /**
     * 是否应用到 deserialize , 默认 true
     */
    boolean de() default true;

    /**
     * 是否应用到 serialize , 默认 true
     */
    boolean se() default true;

    /**
     * json array
     */
    String otherEmptyCases() default "[]";

}
