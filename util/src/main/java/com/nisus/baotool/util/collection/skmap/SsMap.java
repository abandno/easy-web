package com.nisus.baotool.util.collection.skmap;

import com.nisus.baotool.util.etc.Safes;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link HashMap&lt;String,String&gt;} 别名
 * <p>
 *
 * @author L&J
 * @date 2021/9/11 3:53 上午
 */
public class SsMap extends AbstractSMap<String> implements StringKeyMap<String> {

    public static SsMap of(String ... kvs) {
        SsMap soMap = new SsMap();
        return soMap.puts((Object[]) kvs);
    }

    public SsMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public SsMap(int initialCapacity) {
        super(initialCapacity);
    }

    public SsMap() {
    }

    public SsMap(Map<? extends String, ? extends String> m) {
        super(Safes.of(m));
    }

    /**
     * 批量添加 key-value 对
     * <p>
     * ! key 会统一为 String !
     * ! value 会统一为 String !
     *
     * @param kvs
     * @return
     */
    @Override
    public SsMap puts(Object... kvs) {
        int len = kvs.length;
        for (int i = 0; i < len; i++) {
            String key = String.valueOf(kvs[i]);
            ++i;
            Object value = i < len ? kvs[i] : null;
            this.put(key, value == null ? null : value.toString());
        }

        return this;
    }

}
