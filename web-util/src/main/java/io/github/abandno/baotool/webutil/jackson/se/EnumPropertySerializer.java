package io.github.abandno.baotool.webutil.jackson.se;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import io.github.abandno.baotool.core.enums.IEnum;
import io.github.abandno.baotool.util.lang.EnumUtil;
import io.github.abandno.baotool.webutil.jackson.ann.EnumProperty;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

public class EnumPropertySerializer extends JsonSerializer<Serializable> implements ContextualSerializer {

    private EnumProperty enumPropertyAnn;

    // 必须要保留无参构造方法
    public EnumPropertySerializer() {

    }

    public EnumPropertySerializer(EnumProperty enumPropertyAnn) {
        this.enumPropertyAnn = enumPropertyAnn;
    }

    @Override
    public void serialize(Serializable value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        Class<? extends Enum<?>> enumType = enumPropertyAnn.value();

        /*
         * 合法枚举 code 应该是 Number 或 String
         */
        Serializable res = null;
        if (value instanceof Number || value instanceof CharSequence) {
            if (IEnum.class.isAssignableFrom(enumType)) {
                Optional opt = EnumUtil.fromValue((Class) enumType, value);
                if (opt.isPresent()) {
                    res = ((IEnum<?>) opt.get()).getLabel();
                }
            } else { // Enum
                Enum e = EnumUtil.fromStringQuietly((Class) enumType, String.valueOf(value));
                if (e != null) {
                    res = e.name();
                }
            }

            writeValue(value, res, gen);

            return;
        }

        /*
        * 就是说, 不自定义枚举序列化方式, 只要用了 @EnumProperty 注解,
        * 并且符合 IEnum 规范, 也是能享受自动序列化好处的.
        * 值自身就是 Enum , 即自带 Enum Class 信息, 那么, EnumProperty.value 忽略.
        */
        if (value instanceof IEnum) {
            // gen.writeString(((IEnum<?>) value).getLabel());
            writeValue(value, ((IEnum<?>) value).getLabel(), gen);
            return;
        }
        if (value instanceof Enum) {
            String enumName = ((Enum<?>) value).name();
            // gen.writeString(enumName);
            writeValue(value, enumName, gen);
            return;
        }

        // 剩下我不认咯

        JsonSerializer<Object> valueSerializer = provider.findValueSerializer(value.getClass());
        if (valueSerializer != null) {
            valueSerializer.serialize(value, gen, provider);
        }
    }

    void writeValue(Serializable rawVal, Serializable transVal, JsonGenerator gen) throws IOException {
        String labelProperty = enumPropertyAnn.labelProperty();
        if (!labelProperty.isEmpty()) {
            // 现将本字段写出去, 否则后面写新字段报错
            gen.writeObject(rawVal);
            if (transVal == null) {
                gen.writeNullField(labelProperty);
            } else {
                gen.writeStringField(labelProperty, String.valueOf(transVal));
            }
        } else {
            if (transVal == null) {
                gen.writeNull();
            } else {
                gen.writeString(String.valueOf(transVal));
            }
        }
    }

    /**
     * [Jackson使用ContextualSerializer在序列化时获取字段注解的属性 · ScienJus's Blog](https://www.scienjus.com/get-field-annotation-property-by-jackson-contextualserializer/)
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) { // 为空直接跳过
            // EnumField ann = beanProperty.getContextAnnotation(EnumField.class);
            EnumProperty ann = beanProperty.getAnnotation(EnumProperty.class);
            if (ann != null && Enum.class.isAssignableFrom(ann.value())) {
                // Class<? extends Enum<?>> t = ann.value();
                return new EnumPropertySerializer(ann); // 这里的信息是注解的, 所有后续都不会在变化了. 序列化器会被自动缓存起来
            }

            // 不是要处理的类型, 用内置的
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        // 不是我要处理的 null, 用内置的
        return serializerProvider.findNullValueSerializer(beanProperty);
    }
}