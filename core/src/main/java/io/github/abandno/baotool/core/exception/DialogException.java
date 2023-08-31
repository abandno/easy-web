package io.github.abandno.baotool.core.exception;

import java.util.List;

/**
 * 用于前后端交互方便直接弹窗提示 message
 * <p>
 * 1. message 直接是友好话术;
 * 2. 无需打印堆栈错误信息, 仅仅方便程序中断用的.
 * @author L&J
 * @date 2021/10/8 5:28 下午
 */
public class DialogException extends BRuntimeException {
    public DialogException() {
        super();
    }

    public DialogException(String msg) {
        super(msg);
    }

    public DialogException(Throwable cause) {
        super(cause);
    }

    public DialogException(Throwable cause, String msg, Object... args) {
        super(cause, msg, args);
    }

    public DialogException(String msg, Object... args) {
        super(msg, args);
    }

    public DialogException(int code, String msg, Object... args) {
        super(code, msg, args);
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public DialogException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public List<String> getErrors() {
        return this.errors;
    }

    @Override
    public DialogException setErrors(List<String> errors) {
        this.errors = errors;
        return this;
    }
}
