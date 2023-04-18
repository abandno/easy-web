package com.nisus.baotool.core.exception;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 通用异常 RuntimeException
 * <p>
 * 以此作为顶级异常. 其他自定义异常, 请继承此异常.
 *
 * @author L&J
 * @date 2021-10-01 11:08:50
 */
public class BRuntimeException extends RuntimeException implements IException {

    protected Integer code;

    /**
     * 错误信息的进一步展示.
     * 比如, 放上业务关键节点的异常情形说明, 可按调用链路顺序添加.
     */
    protected List<String> errors = new ArrayList<>();
    protected String traceId;

    public BRuntimeException() {
        super();
    }

    public BRuntimeException(String msg) {
        super(msg);
    }

    public BRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * 异常实例放第一个位, 强提示包裹异常, 避免忘记包裹异常, 导致异常堆栈丢失, 影响问题排查
     *
     * @param cause 异常
     * @param msg   消息(或模板)
     * @param args  模板参数. 注意: 最末位异常参数, 只会当作普通参数, 不会设为 cause, 请在 cause 入参显示指定.
     */
    public BRuntimeException(Throwable cause, String msg, Object... args) {
        this(StrUtil.format(msg, args), cause);
    }

    /**
     * @param msg  普通字符串或带"{}"占位符的模板
     * @param args 模板参数. 最后一个参数支持 Throwable 类型, 不会放入模板.
     */
    public BRuntimeException(String msg, Object... args) {
        this(args == null || args.length == 0 ? msg // 正常字符串
                : (args[args.length - 1] instanceof Throwable ?
                StrUtil.format(msg, Arrays.copyOf(args, args.length - 1)) // 末尾是 Throwable
                : StrUtil.format(msg, args))); // 全部是普通 arg
        if (args != null && args.length > 0 && args[args.length - 1] instanceof Throwable) {
            super.initCause((Throwable) args[args.length - 1]);
        }
    }

    // @Deprecated // 不想和响应码耦合
    // public RiskRuntimeException(ErrorCode errorCode, Object... args) {
    //     this(errorCode.getValue(), errorCode.getLabel(), args);
    // }

    /**
     * 方便 service 层方法快速失败, 并经过异常处理器返回友好视图.
     *
     * @param code 业务错误码
     * @param msg
     * @param args
     */
    public BRuntimeException(int code, String msg, Object... args) {
        this(msg, args);
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public BRuntimeException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public List<String> getErrors() {
        return this.errors;
    }

    @Override
    public BRuntimeException setErrors(List<String> errors) {
        this.errors = errors;
        return this;
    }

    @Override
    public BRuntimeException appendErrors(String... errors) {
        return (BRuntimeException) IException.super.appendErrors(errors);
    }

    @Override
    public BRuntimeException appendErrors(Collection<String> errors) {
        return (BRuntimeException) IException.super.appendErrors(errors);
    }

    @Override
    public BRuntimeException appendError(String template, Object... params) {
        return (BRuntimeException) IException.super.appendError(template, params);
    }

    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this).append("message", this.getMessage());
        if (this.code != null) {
            sb.append("code", this.code);
        }
        if (this.errors != null && this.errors.size() > 0) {
            sb.append("errors", this.errors);
        }
        if (this.traceId != null) {
            sb.append("traceId", this.traceId);
        }
        // sb.append("octo", getOctoInfo());
        return sb.toString();
    }


}
