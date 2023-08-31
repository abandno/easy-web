package io.github.abandno.baotool.core.exception;

/**
 * 参数异常
 * <p>
 * 类似 java.lang.IllegalArgumentException
 *
 * @author L&J
 * @version 0.1
 * @since 2023/4/19 9:07
 */
public class ArgumentException extends BRuntimeException {

    public ArgumentException() {
        super();
    }

    public ArgumentException(String msg) {
        super(msg);
    }

    public ArgumentException(Throwable cause) {
        super(cause);
    }

    public ArgumentException(Throwable cause, String msg, Object... args) {
        super(cause, msg, args);
    }

    public ArgumentException(String msg, Object... args) {
        super(msg, args);
    }

    public ArgumentException(int code, String msg, Object... args) {
        super(code, msg, args);
    }
}
