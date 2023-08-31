package io.github.abandno.baotool.util.lang;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import io.github.abandno.baotool.core.enums.IEnum;
import io.github.abandno.baotool.core.exception.BRuntimeException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * 枚举工具
 * <p>
 * 参考 hutool EnumUtil
 *
 * @author L&J
 * @date 2021/9/30 10:52 下午
 */
public class EnumUtil extends cn.hutool.core.util.EnumUtil {

    /**
     * "value" 字段值-->枚举实例
     *
     * 如果实现了 {@link IEnum} 推荐使用此方法根据"value"值查找, 效率更高.
     * @param enumClass 枚举类
     * @param value
     * @param <E> 枚举类泛型
     * @return
     */
    public static <E extends Enum<E>> Optional<E> fromValue(Class<E> enumClass, Serializable value) {
        if (null == enumClass) { //  || value == null
            return Optional.empty();
        }

        E res = null;

        if (IEnum.class.isAssignableFrom(enumClass)) {
            for (final E e : enumClass.getEnumConstants()) {
                if (((IEnum) e).eq(value)) {
                    res = e;
                    break;
                }
            }
        } else if (Enum.class.isAssignableFrom(enumClass)) {
            // 常规 Enum, 使用 name() 匹配
            final E[] enums = enumClass.getEnumConstants();
            for (E anEnum : enums) {
                // 宽松相等
                if (anEnum == value || anEnum.name().equals(value) || anEnum.name().equals(String.valueOf(value))) {
                    res = anEnum;
                    break;
                }
            }
        } else {
            // 反射获取 'value' 字段值
            return fromField(enumClass, "value", value);
        }

        return Optional.ofNullable(res);
    }

    /**
     * 结果非null, null时抛异常
     * @param enumClass 枚举类
     * @param value 枚举实例业务标识ID
     * @param <E> 枚举类
     * @return 非null枚举实例
     */
    public static <E extends Enum<E>> E fromValueOrThrow(Class<E> enumClass, Serializable value) {
        return fromValue(enumClass, value)
                .orElseThrow(() -> new BRuntimeException("Illegal enum code of {}: {}", enumClass, value));
    }

    /**
     * 结果可null, 不抛异常
     * @param enumClass 枚举类
     * @param value 枚举实例业务标识ID
     * @param <E> 枚举类
     * @return 可null枚举实例
     */
    public static <E extends Enum<E>> E fromValueQuietly(Class<E> enumClass, Serializable value) {
        return fromValue(enumClass, value).orElse(null);
    }

    /**
     * 枚举项字段名+值-->枚举
     *
     * 注: 只会返回第一个匹配的. 自己确保唯一语义.
     * @param enumClass 枚举类
     * @param fieldName 搜索的字段名
     * @param value 值
     * @param <E> 枚举类
     * @return
     */
    public static <E extends Enum<E>> Optional<E> fromField(Class<E> enumClass, String fieldName, Object value) {
        return fromField0(enumClass, fieldName, value, false);
    }

    private static <E extends Enum<E>> Optional<E> fromField0(Class<E> enumClass, String fieldName, Object value, boolean isLike) {
        if (value == null || StrUtil.isBlank(fieldName) || enumClass == null) {
            return Optional.empty();
        }
        if (value instanceof CharSequence) {
            value = value.toString().trim();
        }

        final Field field = ReflectUtil.getField(enumClass, fieldName);
        if (field == null) {
            return Optional.empty();
        }

        final Enum<?>[] enums = enumClass.getEnumConstants();

        for (Enum<?> enumObj : enums) {
            Object fieldValue = ReflectUtil.getFieldValue(enumObj, field);
            if (ObjectUtil.equal(value, fieldValue)) {
                return Optional.of((E) enumObj);
            }
            // like 模式下, 尝试都用 string 比较一次
            if (isLike && ObjectUtil.equal(String.valueOf(value), String.valueOf(fieldValue))) {
                return Optional.of((E) enumObj);
            }
        }

        return Optional.empty();
    }

    /**
     * 类似 {@link cn.hutool.core.util.EnumUtil#getEnumMap(Class)}
     * 但, IEnum , 会自动用 getValue() 的值作为 key (注意转成了 String 类型)
     * @param enumClass
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> LinkedHashMap<String, E> getEnumValueMap(final Class<E> enumClass) {
        final LinkedHashMap<String, E> map = new LinkedHashMap<>();
        for (final E e : enumClass.getEnumConstants()) {
            String k = null;
            if (e instanceof IEnum) {
                k = String.valueOf(((IEnum<?>) e).getValue());
            } else {
                k = e.name();
            }

            map.put(k, e);
        }
        return map;
    }

}
