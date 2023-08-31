package io.github.abandno.baotool.core.ext.lang;

import cn.hutool.core.lang.Tuple;

/**
 * <p>
 *
 * @author L&J
 * @date 2021/10/31 2:30 下午
 */
public class Tuple3<T1, T2, T3> extends Tuple {

    public Tuple3(T1 first, T2 second, T3 third) {
        super(first, second, third);
    }

    public Tuple3() {
        this(null, null, null);
    }

    public T1 getFirst() {
        return this.get(0);
    }

    public T2 getSecond() {
        return this.get(1);
    }

    public T3 getThird() {
        return this.get(2);
    }

    public void setFirst(T1 first) {
        super.getMembers()[0] = first;
    }

    public void setSecond(T2 second) {
        super.getMembers()[1] = second;
    }

    public void setThird(T3 third) {
        super.getMembers()[2] = third;
    }
}
