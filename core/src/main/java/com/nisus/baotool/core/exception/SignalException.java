package com.nisus.baotool.core.exception;

import java.util.List;

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

    // public SignalException(Throwable cause) {
    //     super(cause);
    // }

    // public SignalException(Throwable cause, String msg, Object... args) {
    //     super(cause, msg, args);
    // }

    public SignalException(String msg, Object... args) {
        super(msg, args);
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public SignalException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public List<String> getErrors() {
        return this.errors;
    }

    @Override
    public SignalException setErrors(List<String> errors) {
        this.errors = errors;
        return this;
    }

    /**
     * 信号异常, 不打印堆栈
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
