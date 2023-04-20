package com.nisus.baotool.experiment.bean;

import com.nisus.baotool.core.enums.IEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * @author L&J
 * @date 2021/9/3 11:51 上午
 */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {
    private static boolean inited = false;
    private static IEnum<Integer> defaultSuccess = EnumItem.<Integer>builder().value(0).label("成功").build();
    private static IEnum<Integer> defaultFail = EnumItem.<Integer>builder().value(-1).label("失败").build();

    public static synchronized void init(@Nullable IEnum<Integer> defaultSuccess, @Nullable IEnum<Integer> defaultFail) {
        if (inited) {
            // log.info("Result has initialized, return!");
            return;
        }
        if (defaultSuccess != null) {
            Result.defaultSuccess = defaultSuccess;
        }
        if (defaultFail != null) {
            Result.defaultFail = defaultFail;
        }

        inited = true;
    }

    // public static void setDefaultSuccess(IEnum<Integer> defaultSuccess) {
    //     assert defaultSuccess != null;
    //     Result.defaultSuccess = defaultSuccess;
    // }
    //
    // public static void setDefaultFail(IEnum<Integer> defaultFail) {
    //     assert defaultFail != null;
    //     Result.defaultFail = defaultFail;
    // }

    public Integer code;

    public String msg;

    public T data;

    public List<String> errors = new ArrayList<>();

    public String traceId;

    public static <T> Result<T> of(IEnum<Integer> errorCode, T data) {
        Result<T> result = new Result<>();
        result.code = errorCode.getValue();
        result.msg = errorCode.getLabel();
        result.data = data;
        return result;
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        return of(defaultSuccess, data);
    }

    public static <T> Result<T> fail() {
        return fail(defaultFail);
    }

    public static <T> Result<T> fail(IEnum<Integer> errorCode) {
        return of(errorCode, null);
    }

    public static <T> Result<T> fail(String msg) {
        Result<T> of = of(defaultFail, null);
        return of.setMsg(msg);
    }

    public Result() {
        // 团队内有 traceId 功能, 请在这里补上自动获取 traceId 并设置
        // ServerTracer serverTracer = Tracer.getServerTracer();
        // if (serverTracer != null) {
        //     this.traceId = serverTracer.getTraceId();
        // }
    }

    /**
     * @return 是否成功
     */
    public boolean isOk() {
        return defaultSuccess.eq(this.code);
    }

    public Result<T> appendError(String err) {
        this.errors.add(err);
        return this;
    }

    public Result<T> insertError(String err) {
        this.errors.add(0, err);
        return this;
    }

}
