package io.github.abandno.baotool.webutil.jackson.se;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import io.github.abandno.baotool.webutil.jackson.ann.NumberProperty;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 *
 * @author liuhejun
 * @since 2022-01-21 15:18:35
 */
@NoArgsConstructor
public class NumberPropertySerializer extends JsonSerializer<Object> implements ContextualSerializer {

    private JavaType type;
    private NumberProperty numberPropertyAnn;

    public NumberPropertySerializer(NumberProperty ann, JavaType type) {
        this.numberPropertyAnn = ann;
        this.type = type;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String pattern = numberPropertyAnn.pattern();
        if (StrUtil.isNotBlank(pattern)) {
            String fmt = NumberUtil.decimalFormat(pattern, value, numberPropertyAnn.roundingMode());
            gen.writeString(fmt);
            return;
        }

        Number num = null;
        boolean isNum = false;
        if (value instanceof Number) {
            num = (Number) value;
            isNum = true;
        } else if (value instanceof CharSequence) { // 此注解要求必须时合法的字符串数值
            num = NumberUtil.parseNumber(String.valueOf(value));
        } else {
            // 不处理的
            JsonSerializer<Object> valueSerializer = provider.findValueSerializer(value.getClass());
            if (valueSerializer != null) {
                valueSerializer.serialize(value, gen, provider);
            }
            return;
        }

        if (numberPropertyAnn.scale() >= 0) {
            if (num instanceof Double || num instanceof Float) { // 浮点型, 才应用 round 规则
                num = NumberUtil.round((Double) num, numberPropertyAnn.scale(), numberPropertyAnn.roundingMode());
            }
        }

        // 输出
        if (isNum) {
            gen.writeNumber(num.toString());
        } else {
            gen.writeString(num.toString());
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) { // 为空直接跳过
            // EnumField ann = beanProperty.getContextAnnotation(EnumField.class);
            NumberProperty ann = beanProperty.getAnnotation(NumberProperty.class);
            if (ann != null) {
                return new NumberPropertySerializer(ann, beanProperty.getType());
            }

            // 不是要处理的类型, 用内置的
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        // 不是我要处理的 null, 用内置的
        return serializerProvider.findNullValueSerializer(beanProperty);
    }
}
