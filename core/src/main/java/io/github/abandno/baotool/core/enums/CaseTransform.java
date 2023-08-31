package io.github.abandno.baotool.core.enums;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.CaseFormat;
import lombok.AllArgsConstructor;

/**
 * {@link CaseFormat} 枚举类组合简化
 * <p>
 *
 * @author L&J
 * @date 2021-12-15 16:45:46
 */
@AllArgsConstructor
public enum CaseTransform {
    /**
     * 小写驼峰 -> 小写下划线(_)
     */
    LC2LU(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_UNDERSCORE),
    /**
     * 小写驼峰 -> 小写连字符(-)
     */
    LC2LH(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_HYPHEN),
    /**
     * 小写驼峰 -> 大写驼峰
     */
    LC2UC(CaseFormat.LOWER_CAMEL, CaseFormat.UPPER_CAMEL),
    /**
     * 小写驼峰 -> 大写下划线(_)
     */
    LC2UU(CaseFormat.LOWER_CAMEL, CaseFormat.UPPER_UNDERSCORE),
    /**
     * 小写下划线(_) -> 小写驼峰
     */
    LU2LC(CaseFormat.LOWER_UNDERSCORE, CaseFormat.LOWER_CAMEL),
    /**
     * 小写下划线(_) -> 小写连字符(-)
     */
    LU2LH(CaseFormat.LOWER_UNDERSCORE, CaseFormat.LOWER_HYPHEN),
    /**
     * 小写下划线(_) -> 大写驼峰
     */
    LU2UC(CaseFormat.LOWER_UNDERSCORE, CaseFormat.UPPER_CAMEL),
    /**
     * 小写下划线(_) -> 大写下划线(_)
     */
    LU2UU(CaseFormat.LOWER_UNDERSCORE, CaseFormat.UPPER_UNDERSCORE),
    /**
     * 大写驼峰 -> 小写驼峰
     */
    UC2LC(CaseFormat.UPPER_CAMEL, CaseFormat.LOWER_CAMEL),
    /**
     * 大写驼峰 -> 小写下划线(_)
     */
    UC2LU(CaseFormat.UPPER_CAMEL, CaseFormat.LOWER_UNDERSCORE),
    /**
     * 大写驼峰 -> 小写连字符(-)
     */
    UC2LH(CaseFormat.UPPER_CAMEL, CaseFormat.LOWER_HYPHEN),
    /**
     * 大写驼峰 -> 大写下划线(_)
     */
    UC2UU(CaseFormat.UPPER_CAMEL, CaseFormat.UPPER_UNDERSCORE),
    ;
    public final CaseFormat FROM;
    public final CaseFormat TO;

    /**
     * <code>CaseFormat.X.to(CaseFormat.Y, origin)</code> 快捷方式
     * @param origin 原字符串
     * @return 转换后的字符串
     */
    public String transform(String origin) {
        if (StrUtil.isEmpty(origin)) {
            return origin;
        }
        return FROM.to(TO, origin);
    }

}