package com.nisus.baotool.util.etc;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 方便安全取值, 避免NPE
 *
 * @author L&J
 * @date 2022/2/22 11:34 上午
 * <p>
 */
@Slf4j
public class Safes {

    private Safes() {
    }


    public static <T> Iterator<T> of(Iterator<T> source) {
        return ObjectUtil.defaultIfNull(source, Collections.emptyIterator());
    }

    public static <T> Collection<T> of(Collection<T> source) {
        return ObjectUtil.defaultIfNull(source, new ArrayList<>());
    }

    public static <T> Iterable<T> of(Iterable<T> source) {
        return ObjectUtil.defaultIfNull(source, new ArrayList<>());
    }

    public static <T> List<T> of(List<T> source) {
        return ObjectUtil.defaultIfNull(source, new ArrayList<>());
    }

    public static <T> Set<T> of(Set<T> source) {
        return ObjectUtil.defaultIfNull(source, new HashSet<>());
    }

    public static <K, V> Map<K, V> of(Map<K, V> source) {
        return ObjectUtil.defaultIfNull(source, new HashMap<K, V>());
    }

    public static BigDecimal of(BigDecimal source) {
        return ObjectUtil.defaultIfNull(source, BigDecimal.ZERO);
    }

    public static Integer of(Integer source) {
        return source == null ? 0 : source;
    }

    public static Long of(Long source) {
        return source == null ? 0L : source;
    }

    public static Float of(Float source) {
        return source == null ? 0F : source;
    }

    public static Double of(Double source) {
        return source == null ? 0D : source;
    }

    public static String of(String source) {
        return source == null ? "" : source;
    }


    /**
     * 异常时返回默认值
     * @param supplier 原取值逻辑
     * @param defaultValue 默认值
     */
    public static <T> T getQuietly(Supplier<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        } catch (Exception e) {
            // log.warn(e.getMessage());
        }
        return defaultValue;
    }

    public static <R, A> R get(A a, Function<A, R> r) {
        return get(a, r, null);
    }

    /**
     * 安全的链式取值
     * A.R
     *
     * @param a            待取它值的对象
     * @param r            取值函数
     * @param defaultValue null 时降级返回的默认值, 含 原对象 和 取值函数返回的
     * @param <R>          返回值类型
     * @param <A>          原始对象类型
     */
    public static <R, A> R get(A a, Function<A, R> r, R defaultValue) {
        if (a == null) {
            return defaultValue;
        }
        R rVal = r.apply(a);
        return rVal == null ? defaultValue : rVal;
    }

    /**
     * 安全的链式取值
     * A.B.C
     */
    public static <R, A, B> R get(A a, Function<A, B> b, Function<B, R> r, R defaultValue) {
        if (a == null) {
            return defaultValue;
        }
        B bVal = b.apply(a);
        if (bVal == null) {
            return defaultValue;
        }
        R rVal = r.apply(bVal);
        return rVal == null ? defaultValue : rVal;
    }

}
