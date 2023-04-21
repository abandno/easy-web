package com.nisus.baotool.webutil.jackson.de;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.nisus.baotool.core.enums.IEnum;
import com.nisus.baotool.util.lang.EnumUtil;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;

/**
 * 1. 字段类型是 code (字符串或数字). 不处理.
 * 2. 字段类型是 Enum. code -> Enum.
 *   2.1 IEnum code -> Enum
 *   2.2 Enum name -> Enum
 * <pre>
 * // 字段注解
 * \@JsonDeserialize(using = EnumDeserializer.class)
 * private Object e1;
 * \@JsonDeserialize(using = EnumDeserializer.class)
 * private ExampleIEnum e2;
 * \@JsonDeserialize(using = EnumDeserializer.class)
 * private ExampleEnum e3;
 *
 * ----
 * 入参:
 * {
 *   "e1": "111", // 原样 -> "111"
 *   "e2": "2", // IEnum code -> ExampleIEnum.ORDERED
 *   "e3": "AL" // name -> ExampleEnum.AL
 * }
 * </pre>
 *
 * 没法序列化其他字段?
 *
 * - 支持 Enum List, Enum Array 字段 (since 1.1.9)
 * - 支持 hibernate 注解. 先转换, 后校验. 入参空或转换后空(非法枚举值)都会触发校验失败.
 * - 注意不要使用首字母大写的字段名, 这样生成的 setter 还是 setAbc, 请求参数还是要 'abc' 小写开头.
 * @author liuhejun
 * @since 2022-06-27 21:54:50
 */
@NoArgsConstructor
public class EnumDeserializer extends JsonDeserializer<Enum> implements ContextualDeserializer {

    private JavaType type;

    public EnumDeserializer(JavaType type) {
        this.type = type;
    }

    @Override
    public Enum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Object value = ctxt.readValue(p, Object.class);

        JavaType type1 = getJavaType();

        Class<?> rawClass = type1.getRawClass();
        if (Enum.class.isAssignableFrom(rawClass) && value instanceof Serializable) {
            if (IEnum.class.isAssignableFrom(rawClass)) {
                Object enu = EnumUtil.fromValue((Class) rawClass, (Serializable) value).orElse(null);
                return (Enum) enu;
            } else {
                Enum enu = EnumUtil.fromStringQuietly((Class) rawClass, String.valueOf(value));
                return enu;
            }
        }

        return (Enum) ctxt.findNonContextualValueDeserializer(type).deserialize(p, ctxt);
    }

    private JavaType getJavaType() {
        JavaType type1;
        if (type instanceof CollectionType || type instanceof ArrayType) {
            type1 = type.getContentType();
        } else {
            type1 = type;
        }
        return type1;
    }


    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) { // 为空直接跳过
            JavaType type = beanProperty.getType();
            return new EnumDeserializer(type);
        }
        // 不是我要处理的 null, 用内置的
        return ctxt.findNonContextualValueDeserializer(ctxt.getContextualType()); // ??
    }
}
