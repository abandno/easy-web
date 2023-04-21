package com.nisus.baotool.webutil.jackson.de;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.nisus.baotool.util.lang.EmptyUtil;
import com.nisus.baotool.webutil.jackson.ann.EmptyToNullProperty;
import com.nisus.baotool.webutil.jackson.se.EmptyToNullPropertySerializer;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 *
 * 类似 {@link EmptyToNullPropertySerializer}
 *
 * 注意: @RequestBody 入参才会有作用; 使用 EmptyUtil.isEmpty 判空.
 * @author liuhejun
 * @since 2022-05-07 22:55:13
 */
@NoArgsConstructor
public class EmptyToNullPropertyDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {

    private JavaType type;
    private Object[] otherEmptyCases;

    public EmptyToNullPropertyDeserializer(Object[] otherEmptyCases, JavaType type) {
        this.otherEmptyCases = otherEmptyCases;
        this.type = type;
    }


    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Object value = ctxt.readValue(p, type);
        if (EmptyUtil.isEmpty(value, otherEmptyCases)) {
            return null;
        } else {
            return value;
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) { // 为空直接跳过
            EmptyToNullProperty ann = beanProperty.getAnnotation(EmptyToNullProperty.class);
            if (ann != null) {
                if (ann.de()) {
                    Object[] otherEmptyCases = null;
                    if (StrUtil.isNotBlank(ann.otherEmptyCases())) {
                        otherEmptyCases = JSON.parseArray(ann.otherEmptyCases()).toArray();
                    }

                    return new EmptyToNullPropertyDeserializer(otherEmptyCases, beanProperty.getType());
                } // else: 明确指定了 de 不应用, 用内置默认的
            } else {
                // 没有 EmptyToNullProperty 注解, 但走到这里, 说明使用方式:
                // @JsonDeserialize(using = EmptyToNullPropertyDeserializer.class)
                return new EmptyToNullPropertyDeserializer(new Object[]{}, beanProperty.getType());
            }
        }
        // 不是我要处理的 null, 用内置的
        return ctxt.findNonContextualValueDeserializer(ctxt.getContextualType()); // ??
    }
}
