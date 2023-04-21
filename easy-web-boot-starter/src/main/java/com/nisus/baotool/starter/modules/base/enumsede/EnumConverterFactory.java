package com.nisus.baotool.starter.modules.base.enumsede;

import com.nisus.baotool.core.enums.IEnum;
import com.nisus.baotool.util.lang.EnumUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *- 支持 Enum List 字段
 * - 支持 hibernate 注解
 * @author L&J
 * @version 0.1
 * @since 2022/7/1 2:44 下午
 */
public class EnumConverterFactory implements ConverterFactory<String, Enum> {

    private final static Map<Class, Converter> CONVERTER_CACHE = new ConcurrentHashMap<>();

    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        Converter converter = CONVERTER_CACHE.computeIfAbsent(targetType, k -> {
            return new EnumConverter(k);
        });
        return converter;
    }


    /**
     * <p>
     * 枚举编码 -> 枚举 转化器
     * </p>
     * [Spring Boot 使用枚举类型作为请求参数 | CodingDiary](https://xkcoding.com/2019/01/30/spring-boot-request-use-enums-params.html)
     */
    public static class EnumConverter<T extends Enum<T>> implements Converter<String, T> {
        private Class<T> enumType;

        public EnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            if (IEnum.class.isAssignableFrom(enumType)) {
                return EnumUtil.fromValue(enumType, source).orElse(null);
            }
            // Enum ?
            if (Enum.class.isAssignableFrom(enumType)) {
                return EnumUtil.fromStringQuietly(enumType, source);
            }

            return null;
        }
    }
}
