package io.github.abandno.baotool.util.collection.skmap;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link HashMap&lt;String,T&gt;} 别名
 * <p>
 *
 * @author L&J
 * @date 2021/9/11 3:53 上午
 */
public class SMap<T> extends AbstractSMap<T> implements StringKeyMap<T> {

    public SMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public SMap(int initialCapacity) {
        super(initialCapacity);
    }

    public SMap() {
    }

    public SMap(Map<? extends String, T> m) {
        super(m);
    }

    /**
     * 为了避免可能和 T 类型不一致, 不支持此方法
     */
    @Override
    public SMap<T> puts(Object... kvs) {
        throw new UnsupportedOperationException();
    }

}
