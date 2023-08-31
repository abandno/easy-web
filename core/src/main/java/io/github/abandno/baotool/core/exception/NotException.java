package io.github.abandno.baotool.core.exception;

/**
 * 很明显我不是个'异常'的异常, 为了便于预期内的程序中断.
 * 比如, 重试场景, 未到重试上限, 没成功, 只想输出 info , 而不是 error 或异常,
 * 减少不重要的异常干扰.
 * @author L&J
 * @version 0.1
 * @since 2022/8/22 8:40 上午
 */
public class NotException extends SignalException {
    public NotException() {
        super();
    }

    public NotException(String msg) {
        super(msg);
    }

    public NotException(String msg, Object... args) {
        super(msg, args);
    }



}
