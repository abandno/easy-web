package io.github.abandno.baotool.util.reflect;

import io.github.abandno.baotool.core.exception.BRuntimeException;
import io.github.abandno.baotool.util.function.SFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * @author L&J
 * @date 2021/10/2 3:27 下午
 */
public class LambdaUtil {

    /**
     * SerializedLambda 反序列化缓存
     */
    private static final Map<Class<?>, WeakReference<SerializedLambda>> FUNC_CACHE = new ConcurrentHashMap<>();

    private static final String WRITE_REPLACE = "writeReplace";

    /**
     * Get SerializedLambda
     * <p>
     * Q: Function 不行, 自己定义的 Fn 就可以<br>
     * A: 关键是 Serializable 接口, 才有 writeReplace 方法.
     * <p>
     * 如果一个序列化类中含有Object writeReplace()方法，那么实际序列化的对象将是作为 writeReplace 方法返回值的对象，
     * 而且序列化过程的依据是该返回对象的序列化实现。
     * 就是说, A.writeReplace return B, 那么序列化 A 时, 实际序列化的将是 B . 和 A 无关.
     * 正式 "替换写" 的语义.
     * 这样, Fn 作为 Lambda , (1)存在 writeReplace 方法, (2)该方法返回 SerializedLambda .
     * 故, 拿到该方法, 进而可以去到字段名(Lambda元数据之一).
     *
     * @param fn Lambda
     */
    public static <T> SerializedLambda getSerializedLambda(SFunction<T, ?> fn) {
        // User::getName 和 User::getAge , class 不同
        Class<? extends SFunction> clazz = fn.getClass();
        if (!clazz.isSynthetic()) {
            throw new BRuntimeException("非法 lambda: {}, class: {}. SFunction.getClass().isSynthetic() 必须 true, 如: User::getName.", fn, clazz);
        }

        return Optional.ofNullable(FUNC_CACHE.get(clazz))
                .map(WeakReference::get)
                .orElseGet(() -> {
                    SerializedLambda lambda = null;
                    try {
                        Method method = clazz.getDeclaredMethod(WRITE_REPLACE);
                        method.setAccessible(Boolean.TRUE);
                        lambda = (SerializedLambda) method.invoke(fn);
                        method.setAccessible(Boolean.FALSE);
                    } catch (Exception e) {
                        throw new BRuntimeException("method `writeReplace` call fail, get SerializedLambda of `" + clazz.getName() + "` fail", e);
                    }
                    FUNC_CACHE.put(clazz, new WeakReference<>(lambda));
                    return lambda;
                });
    }

}
