package com.nisus.baotool.util.collection.skmap;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.TypeUtil;
import com.nisus.baotool.core.exception.BRuntimeException;
import com.nisus.baotool.util.etc.Safes;
import com.nisus.baotool.util.function.SFunction;
import com.nisus.baotool.util.reflect.FieldNameUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * @author L&J
 * @version 0.1
 * @since 2022/11/15 13:36
 */
public abstract class AbstractSMap<T> extends HashMap<String, T> implements StringKeyMap<T> {

    private Type valueType;

    private void initialize() {
        this.valueType = TypeUtil.getTypeArgument(getClass());
    }

    public AbstractSMap() {
        initialize();
    }

    public AbstractSMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        initialize();
    }

    public AbstractSMap(int initialCapacity) {
        super(initialCapacity);
        initialize();
    }

    public AbstractSMap(Map<? extends String, ? extends T> m) {
        super(Safes.of(m));
        initialize();
    }

    /**
     * 解析 getter lambda 得到 key, 然后取值
     */
    public <B> T get(SFunction<B,?> key) {
        String strKey = FieldNameUtil.get(key);
        return this.get(strKey);
    }

    public <B> T put(SFunction<B, ?> key, T value) {
        String strKey = FieldNameUtil.get(key);
        return this.put(strKey, value);
    }

    /**
     * 批量添加 key-value 对
     * <p>
     * ! key 会统一为 String !
     * ! value 根据泛型 T 转换, 务必自己保证类型正常转换 !
     *
     * @param kvs key 转成 String, value 尝试转泛型, 失败抛异常.
     */
    @Override
    public AbstractSMap<T> puts(Object... kvs) {
        int len = kvs.length;
        for (int i = 0; i < len; i++) {
            String key = String.valueOf(kvs[i]);
            ++i;
            Object value = i < len ? kvs[i] : null;

            // jdk 类型可以自动转换, 其他类型需要保证类型正确, 否则 cast 异常
            T castedValue;
            try {
                Object converted = Convert.convert(valueType, value);
                castedValue = (T) converted;
            } catch (Exception e) {
                throw new BRuntimeException(e, "convert type of value `{}` to type `{}` error, key: {}", value, valueType, key);
            }

            this.put(key, castedValue);
        }

        return this;
    }

}
