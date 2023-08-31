package io.github.abandno.baotool.core.exception;

import java.util.function.Supplier;

/**
 * 提供异常的 lambda 表达式
 * <p>
 *
 * @author L&J
 * @date 2021/10/8 5:41 下午
 */
public class Exceptions {

    /**
     * 提供异常 lambda 快捷方式
     * <pre>
     * // 下面是此方法的典型用法, 不用手写 lambda 表达式: () -> new RiskException(...). 当然差不多啦, 看您喜好
     * Assert.notNull(obj, ErrorSupplier.riskRuntimeException("obj 不能为null"));
     * </pre>
     * @param msg
     * @param args
     * @return
     */
    public static Supplier<BRuntimeException> runtimeException(String msg, Object... args) {
        return () -> new BRuntimeException(msg, args);
    }

    public static Supplier<DialogException> dialogException(String msg, Object... args) {
        return () -> new DialogException(msg, args);
    }

    public static Supplier<RequestException> requestException(String msg, Object... args) {
        return () -> new RequestException(msg, args);
    }

    public static Supplier<BException> exception(String msg, Object... args) {
        return () -> new BException(msg, args);
    }


    /**
     * 将 checked 异常 (Exception) 偷偷抛出(原样抛出)
     *
     * <pre>
     *  try {
     *      throw new Exception("checked 异常");
     *  } catch (Exception e) {
     *      return sneakyThrow(e); // 原样抛出, 但编译通过(神奇!!!); return 根据需要可有可无
     *      // throw e; // Unhandled exception: java.lang.Exception
     *  }
     * </pre>
     */
    public static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }



}
