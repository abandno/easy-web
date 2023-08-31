package io.github.abandno.baotool.core.ext.lang;

import cn.hutool.core.lang.Tuple;

/**
 * <p>
 *
 * @author L&J
 * @date 2021/10/26 5:24 下午
 */
public class Tuple2<T1, T2> extends Tuple {

    public Tuple2(T1 first, T2 second) {
        super(first, second);
    }

    public Tuple2() {
        this(null, null);
    }

    public T1 getFirst() {
        return this.get(0);
    }

    public T2 getSecond() {
        return this.get(1);
    }

    public void setFirst(T1 first) {
        super.getMembers()[0] = first;
    }

    public void setSecond(T2 second) {
        super.getMembers()[1] = second;
    }

}
