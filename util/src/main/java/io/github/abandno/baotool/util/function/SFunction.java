package io.github.abandno.baotool.util.function;

import java.io.Serializable;
import java.util.function.Function;

/**Serializable Function
 * <p>
 *
 * @author L&J
 * @date 2021/10/2 3:14 下午
 */
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
