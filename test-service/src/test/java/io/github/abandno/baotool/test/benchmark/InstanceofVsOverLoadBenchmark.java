package io.github.abandno.baotool.test.benchmark;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * @author nisus
 * @version 0.1
 * @since 2023/4/20 14:08
 */
public class InstanceofVsOverLoadBenchmark {

    static Object[] subjects = new Object[]{
            123L,
            "abc",
            new Date(),
            Lists.newArrayList(1, 2),
            Maps.newHashMap("a", 2),
            new String[]{"a"}
    };

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Threads(10) // 多少个线程执行
    @Fork(1) // 进程数
    @BenchmarkMode(Mode.Throughput) // 模式
    @Measurement(iterations = 5)
    // @Test
    public void test1() {
        isEmptyByInstanceof(subjects[0]);
        isEmptyByInstanceof(subjects[1]);
        isEmptyByInstanceof(subjects[2]);
        isEmptyByInstanceof(subjects[3]);
        isEmptyByInstanceof(subjects[4]);
        isEmptyByInstanceof(subjects[5]);
    }

    @Benchmark
    @Warmup(iterations = 1, time = 1)
    @Threads(10) // 多少个线程执行
    @Fork(1) // 进程数
    @BenchmarkMode(Mode.Throughput) // 模式
    @Measurement(iterations = 5)
    public void test2() {
        isEmptyByOverload((Number) subjects[0]);
        isEmptyByOverload((CharSequence) subjects[1]);
        isEmptyByOverload((Date) subjects[2]);
        isEmptyByOverload((Collection) subjects[3]);
        isEmptyByOverload((Map) subjects[4]);
        isEmptyByOverload((Object[]) subjects[5]);
    }


    public boolean isEmptyByInstanceof(Object subject) {
        boolean flg = false;
        if (subject instanceof Number) {
            flg = NumberUtil.equals(new BigDecimal(subject.toString()), BigDecimal.ZERO);
        } else if (subject instanceof CharSequence) {
            String strSubject = String.valueOf(subject);
            flg = StrUtil.isBlank((CharSequence) subject) || "null".equalsIgnoreCase(strSubject); // || "0".equals(strSubject);
        } else if (subject instanceof Date) {
            flg = ((Date) subject).getTime() == 0;
        } else if (subject instanceof Collection) {
            flg = ((Collection<?>) subject).isEmpty();
        } else if (subject instanceof Map) {
            flg = ((Map<?, ?>) subject).isEmpty();
        } else if (ArrayUtil.isArray(subject)) {
            // flg = ArrayUtil.isEmpty((Object[]) subject);
            flg = ((Object[]) subject).length == 0;
        }

        return flg;
    }

    public boolean isEmptyByOverload(Number subject) {
        boolean flg = false;
        if (subject != null) {
            flg = NumberUtil.equals(new BigDecimal(subject.toString()), BigDecimal.ZERO);
        }
        return flg;
    }
    public boolean isEmptyByOverload(CharSequence subject) {
      boolean flg = false;
        if (subject != null) {
            String strSubject = String.valueOf(subject);
            flg = StrUtil.isBlank((CharSequence) subject) || "null".equalsIgnoreCase(strSubject); // || "0".equals(strSubject);
        }
        return flg;
    }
    public boolean isEmptyByOverload(Date subject) {
      boolean flg = false;
        if (subject != null) {
            flg = ((Date) subject).getTime() == 0;
        }
        return flg;
    }
    public boolean isEmptyByOverload(Collection subject) {
      boolean flg = false;
        if (subject != null) {
            flg = ((Collection<?>) subject).isEmpty();
        }
        return flg;
    }
    public boolean isEmptyByOverload(Map subject) {
      boolean flg = false;
        if (subject != null) {
            flg = ((Map<?, ?>) subject).isEmpty();
        }
        return flg;
    }

    public boolean isEmptyByOverload(Object[] subject) {
        boolean flg = false;
        if (subject != null) {
            flg = ((Object[]) subject).length == 0;
        }
        return flg;
    }


}
