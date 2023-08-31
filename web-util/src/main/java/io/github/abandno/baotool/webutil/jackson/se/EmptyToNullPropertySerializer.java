package io.github.abandno.baotool.webutil.jackson.se;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import io.github.abandno.baotool.util.lang.EmptyUtil;
import io.github.abandno.baotool.webutil.jackson.ann.EmptyToNullProperty;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * empty -> null
 * <p>
 * 有时序列化, 需要将一些零值转成null, 前端方便渲染.
 * 默认使用 <code>EmptyUtil.isEmpty</code> 判空.
 * 其他定制空case, 需要 otherEmptyCases 指定(json array)
 *
 * @author liuhejun
 * @since 2022-02-22 17:54:19
 */
@NoArgsConstructor
public class EmptyToNullPropertySerializer extends JsonSerializer<Object> implements ContextualSerializer {

    private JavaType type;
    private Object[] otherEmptyCases;

    public EmptyToNullPropertySerializer(Object[] otherEmptyCases, JavaType type) {
        this.otherEmptyCases = otherEmptyCases;
        this.type = type;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (EmptyUtil.isEmpty(value, otherEmptyCases)) {
            gen.writeNull();
        } else {
            gen.writeObject(value);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) { // 为空直接跳过
            EmptyToNullProperty ann = beanProperty.getAnnotation(EmptyToNullProperty.class);
            if (ann != null) { // 有注解信息 & 应用序列化
                if (ann.se()) {
                    Object[] otherEmptyCases = null;
                    if (StrUtil.isNotBlank(ann.otherEmptyCases())) {
                        otherEmptyCases = JSON.parseArray(ann.otherEmptyCases()).toArray();
                    }

                    return new EmptyToNullPropertySerializer(otherEmptyCases, beanProperty.getType());
                } // else: 明确指定不应用 se , 用内置默认的
            } else {
                // EmptyToNullProperty null 还走到这里, 说明使用方式:
                // @JsonSerialize(using = EmptyToNullPropertySerializer.class)
                return new EmptyToNullPropertySerializer(null, beanProperty.getType());
            }
        }
        // 不是我要处理的 null, 用内置的
        return serializerProvider.findNullValueSerializer(beanProperty);
    }
}
