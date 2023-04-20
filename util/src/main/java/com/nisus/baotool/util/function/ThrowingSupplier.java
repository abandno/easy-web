package com.nisus.baotool.util.function;


import com.nisus.baotool.core.exception.BRuntimeException;
import com.nisus.baotool.core.exception.Exceptions;

import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Copy from com.pivovarit.function.ThrowingSupplier,
 * 但 Exception 改为了更高的 Throwable.
 * 因部分工具包的 lambda 表达式抛出的是 Throwable.
 *
 * <pre>
 * // 本地能够编译运行, 但 plus 部署可能报错
 * Optional.of().orElseThrow(new RuntimeException("xxx"));
 * // 改为:
 * Optional.of().<RuntimeException></>orElseThrow(new RuntimeException("xxx"));
 *</pre>
 *
 * Represents a function that accepts zero arguments and returns some value.
 * Function might throw a checked exception instance.
 *
 * @param <T> the type of the output to the function
 * @param <E> the type of the thrown checked exception
 * @author Grzegorz Piwowarek; L&J
 * @since 2022-04-30 16:31:05
 */
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Throwable> {
    T get() throws E;

    // /**
    //  * @return this Consumer instance as a new Function instance
    //  */
    // default ThrowingFunction<Void, T, E> asFunction() {
    //     return arg -> get();
    // }

    static <T> Supplier<T> unchecked(ThrowingSupplier<T, ?> supplier) {
        return requireNonNull(supplier).uncheck();
    }

    static <T> Supplier<Optional<T>> lifted(ThrowingSupplier<T, ?> supplier) {
        return requireNonNull(supplier).lift();
    }

    /**
     * Returns a new Supplier instance which rethrows the checked exception using the Sneaky Throws pattern
     * @return Supplier instance that rethrows the checked exception using the Sneaky Throws pattern
     */
    static <T1> Supplier<T1> sneaky(ThrowingSupplier<T1, ?> supplier) {
        requireNonNull(supplier);
        return () -> {
            try {
                return supplier.get();
            } catch (final Throwable ex) {
                // return SneakyThrowUtil.sneakyThrow(ex);
                return Exceptions.sneakyThrow(ex);
            }
        };
    }

    /**
     * @return a new Supplier instance which wraps thrown checked exception instance into a RuntimeException
     */
    default Supplier<T> uncheck() {
        return () -> {
            try {
                return get();
            } catch (final Throwable e) {
                // wrap it
                throw new BRuntimeException(e);
            }
        };
    }

    /**
     * @return a new Supplier that returns the result as an Optional instance. In case of a failure or null, empty Optional is
     * returned
     */
    default Supplier<Optional<T>> lift() {
        return () -> {
            try {
                return Optional.ofNullable(get());
            } catch (final Throwable e) {
                return Optional.empty();
            }
        };
    }
}
