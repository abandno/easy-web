package com.nisus.baotool.webutil.jackson.se;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.nisus.baotool.util.lang.EmptyUtil;

import java.io.IOException;

/**
 * empty -> null
 * <p>
 * 有时序列化, 需要将一些零值转成null, 前端方便渲染.
 * 默认使用  {@link EmptyUtil#isEmpty} 判空.
 * @author L&J
 * @version 0.1
 * @since 2022/1/12 5:06 下午
 */
public class EmptyToNullSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (EmptyUtil.isEmpty(value)) {
            gen.writeNull();
        } else {
            gen.writeObject(value);
        }
    }
}
