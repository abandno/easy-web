package com.nisus.baotool.util.collection;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * <p>
 * 1. 不接收空串的 key, 无异常, 但会忽略, 不添加.
 * @author L&J
 * @version 0.1
 * @since 2022/4/29 11:13 下午
 */
public class NameValuePair implements Map<String, String> {
    private final Map<String, String> pairs;

    public NameValuePair() {
        this(false);
    }

    /**
     * @param sorted 是否对 Key 按字典序排序; 默认按添加的顺序
     */
    public NameValuePair(boolean sorted) {
        if (sorted) {
            pairs = new TreeMap<>();
        } else {
            pairs = new LinkedHashMap<>();
        }
    }

    public void puts(Object... kvs) {
        if (kvs != null) {
            int len = kvs.length;
            for (int i = 0; i < len; i++) {
                String key = String.valueOf(kvs[i]);
                ++i;
                Object value = i < len ? kvs[i] : null;
                this.put(key, value == null ? null : value.toString());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        pairs.forEach((k, v) -> {
            if (StrUtil.isBlank(k)) {
                return;
            }
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(k);
            sb.append("=").append(ObjectUtil.defaultIfNull(v, ""));

        });
        return sb.toString();
    }

    public static NameValuePair from(String nameValuePair) {
        NameValuePair inst = new NameValuePair();
        if (StrUtil.isBlank(nameValuePair)) {
            return inst;
        }
        nameValuePair = nameValuePair.trim();
        Arrays.stream(nameValuePair.split("&")).forEach(it -> {
            int ix = it.indexOf("=");
            String key = ix > 0 ? it.substring(0, ix) : it;
            if (StrUtil.isBlank(key)) {
                return;
            }
            String value = ix > 0 && it.length() > ix + 1 ? it.substring(ix + 1) : null;
            inst.put(key, value);
        });

        return inst;
    }


    @Override
    public int size() {
        return this.pairs.size();
    }

    @Override
    public boolean isEmpty() {
        return this.pairs.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.pairs.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.pairs.containsValue(value);
    }

    @Override
    public String get(Object key) {
        return this.pairs.get(key);
    }

    /**
     * @param key   不接收空串, 空串, 则此键值对不会被添加
     * @param value value to be associated with the specified key
     */
    @Override
    public String put(String key, String value) {
        if (StrUtil.isBlank(key)) {
            return null;
        }
        return this.pairs.put(key, value);
    }

    @Override
    public String remove(Object key) {
        return this.pairs.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> map) {
        // this.pairs.putAll(map);
        if (map == null) {
            return;
        }
        map.forEach(this::put);
    }

    @Override
    public void clear() {
        this.pairs.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.pairs.keySet();
    }

    @Override
    public Collection<String> values() {
        return this.pairs.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return this.pairs.entrySet();
    }
}
