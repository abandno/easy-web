package io.github.abandno.baotool.core.exception;

import java.util.List;

/**
 * 请求错误异常
 * <p>
 * 用于入参校验不通过等场景
 *
 * @author L&J
 * @since 2022-02-23 15:19:51
 */
public class RequestException extends BRuntimeException {
    public RequestException() {
        super();
    }

    public RequestException(String msg) {
        super(msg);
    }

    public RequestException(Throwable cause) {
        super(cause);
    }

    public RequestException(Throwable cause, String msg, Object... args) {
        super(cause, msg, args);
    }

    public RequestException(String msg, Object... args) {
        super(msg, args);
    }

    public RequestException(int code, String msg, Object... args) {
        super(code, msg, args);
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public RequestException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public List<String> getErrors() {
        return this.errors;
    }

    @Override
    public RequestException setErrors(List<String> errors) {
        this.errors = errors;
        return this;
    }
}
