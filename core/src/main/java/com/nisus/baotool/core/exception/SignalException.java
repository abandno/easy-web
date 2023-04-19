package com.nisus.baotool.core.exception;

/**
 * '信号'异常, 仅作为中断流程, 不打印堆栈
 * <p>
 * cause 属性如果非null, 也还是会打印堆栈. 故, 不支持设置 cause .
 * 以契合本异常类定位.
 * 如果非要设置 cause , initCause 可以做到.
 * @author L&J
 * @version 0.1
 * @since 2022/8/22 8:40 上午
 */
public class SignalException extends BRuntimeException {
    public SignalException() {
        super();
    }

    public SignalException(String msg) {
        super(msg);
    }

    public SignalException(String msg, Object... args) {
        super(msg, args);
    }

    /**
     * 信号异常, 不打印堆栈
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
