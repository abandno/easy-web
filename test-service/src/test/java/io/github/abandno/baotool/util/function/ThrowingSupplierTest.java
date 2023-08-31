package io.github.abandno.baotool.util.function;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Supplier;

public class ThrowingSupplierTest {

    @Test
    void test1() {
        // jdk 内置 Supplier 是不支持抛出检查异常的
        Supplier<Exception> exceptionSupplier = () -> {
            // Unhandled exception: java.lang.Exception
            // 提示你需要 try...catch
            // throw new Exception("check exception");
            // RuntimeException 则可以
            throw new RuntimeException("uncheck exception");
        };

        // exceptionSupplier.get(); // throw RuntimeException

        ThrowingSupplier<String, Exception> exceptionSupplier2 = () -> {
            // 这里却可以
            throw new Exception("check exception");
        };

        try {
            exceptionSupplier2.get(); // 是要 try...catch
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Supplier<String> exceptionSupplier3 = exceptionSupplier2.uncheck();

        // exceptionSupplier3.get(); // 不需要 try...catch

        // 异常时不抛出异常, 而是返回 Optional.empty()
        Optional<String> optionalResult = exceptionSupplier2.lift().get();
        System.out.println(optionalResult.orElse(null));

        // 静态工具方法
        // Exception -> BRuntimeException 抛出
        ThrowingSupplier.unchecked(() -> {
            // 通过 unchecked 包裹, 这里就可以抛出 check Exception 了
            throw new Exception("check exception");
        });

        // 偷偷抛出异常, 原样抛出, 即还是抛出 Exception
        ThrowingSupplier.sneaky(() -> {
            throw new Exception("check exception");
        });

    }


}
