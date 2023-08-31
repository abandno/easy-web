package io.github.abandno.baotool.experiment;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import io.github.abandno.baotool.core.consts.StringPool;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 未分类的工具方法
 * <p>
 *
 * @author L&J
 * @version 0.1
 * @since 2022/7/29 8:37 下午
 */
public class Utils {


    /**
     * null -> null, 而不是 'null'
     *
     * @param obj object
     */
    public static String toString(Object obj) {
        return toString(obj, null);
    }

    /**
     * @param defaultValue null 时的替代值
     */
    public static String toString(Object obj, String defaultValue) {
        return obj == null ? defaultValue : String.valueOf(obj);
    }

    /**
     * `NumberUtil.toDouble` 增强, 入参可 null.
     *
     * @param value 可 null
     */
    public static Double toDouble(Number value) {
        return toDouble(value, null);
    }

    /**
     * @param defaultValue null 值替换
     */
    public static Double toDouble(Number value, Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        // 入参不可null
        return NumberUtil.toDouble(value);
    }

    /**
     * Number 格式化, 小数位精度的基础上, 舍去 0
     */
    public static String round(Number number, int scale) {
        if (number == null) {
            return null;
        }
        String fmt = "#." + StrUtil.repeat("#", scale);
        DecimalFormat decimalFormat = new DecimalFormat(fmt);
        return decimalFormat.format(number);
    }

    /**
     * 不相等后, 降级转为 str 再比较
     *
     * @param a 对象1
     * @param b 对象2
     */
    public static boolean equals(Object a, Object b) {
        return Objects.equals(a, b) || (a != null && b != null && a.toString().equals(b.toString()));
    }

    /**
     * contains
     * 比较时忽略 数值|字符 差异
     */
    public static <E> boolean contains(Collection<E> coll, Object target) {
        if (coll == null) {
            return false;
        }
        for (E e : coll) {
            if (equals(target, e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 长字符截取
     * @param raw 原始字符串
     * @param maxLen 最大长度限制
     */
    public static String truncate(String raw, int maxLen) {
        return truncate(raw, maxLen, StringPool.EMPTY);
    }

    /**
     * 长字符截取
     * <pre>
     * assertTrue(Utils.truncate(null, 100) == null);
     * assertEquals(Utils.truncate("abc", 100), "abc");
     * assertEquals(Utils.truncate("abc", 3, "<...>"), "abc");
     * assertEquals(Utils.truncate("abc123eftljlj", 5, "<...>"), "abc12");
     * assertEquals(Utils.truncate("abc123eftljlj", 6, "<...>"), "a<...>");
     * assertEquals(Utils.truncate("abc123eftljlj", 10, "<...>"), "abc12<...>");
     * </pre>
     *
     * @param raw     原始字符串
     * @param maxLen  最大长度限制. 截取后长度就是 maxLen.
     * @param omitTag 尾部追加的标识, 代表被截取过. 注意, omitTag.length 达到 maxLen 时不会追加
     */
    public static String truncate(String raw, int maxLen, String omitTag) {
        if (raw == null) {
            return raw;
        }
        if (maxLen < 0) {
            maxLen = 0;
        }
        if (omitTag == null) {
            omitTag = StringPool.EMPTY;
        }

        if (raw.length() > maxLen) {
            // 截取
            if (omitTag.length() >= maxLen) {
                // omitTag 过长, 也不追加
                return raw.substring(0, maxLen);
            }

            return raw.substring(0, maxLen - omitTag.length()) + omitTag;
        }

        return raw;
    }

    /**
     * 消除 cast 警告 (自己保证类型正确)
     *
     * @param list 集合
     * @param <T>  原类型
     * @param <R>  cast 后的类型
     */
    public static <T, R> List<R> cast(List<T> list) {
        @SuppressWarnings("unchecked")
        List<R> cast = (List<R>) list;
        return cast;
    }

    /**
     * 消除 cast 警告 (自己保证类型正确)
     */
    public static <T, R> R cast(T obj) {
        @SuppressWarnings("unchecked")
        R obj1 = (R) obj;
        return obj1;
    }


}
