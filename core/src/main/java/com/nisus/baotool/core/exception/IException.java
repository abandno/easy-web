package com.nisus.baotool.core.exception;

import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 异常接口定义
 * <p>
 *
 * M 2022-09-25 17:10:33 <br>
 * 由于, JDK 异常类是不允许放置泛型的. 故, 具体定制异常类是没法传递泛型到这个接口的.
 * 泛型则显得鸡肋, 干脆去掉.
 * 链式编程时, 如需要实际类型, 请显示强转类型吧(基本是安全的).
 * @author L&J
 * @date 2021/10/3 10:58 下午
 */
public interface IException {

    /**
     * 异常类一般自带
     * <p>
     * {@code Throwable#detailMessage}
     * @return 默认是 detailMessage , 可能为 null .
     */
    String getMessage();

    /**
     * 获取响应码
     * @return 响应码
     */
    Integer getCode();

    /**
     * 设置响应码
     * @param code 响应码
     * @return this
     */
    IException setCode(Integer code);

    /**
     * 获取 errors 列表
     * @return errors 列表, 建议非 null .
     */
    List<String> getErrors();

    /**
     * 设置 errors 列表
     * @param errors 设置 errors 列表
     * @return this
     */
    IException setErrors(List<String> errors);

    /**
     * 追加 errors, 已有的 null , 则自动初始化列表
     */
    default IException appendErrors(String... errors) {
        return this.appendErrors(Arrays.asList(errors));
    }

    default IException appendErrors(Collection<String> errors) {
        if (this.getErrors() == null) {
            synchronized (this) {
                if (this.getErrors() == null) {
                    this.setErrors(new ArrayList<>());
                }
            }
        }
        this.getErrors().addAll(errors);
        return this;
    }

    /**
     * 追加一个 error 信息
     * @param template 带占位符'{}'的字符串模板, 如 'abc{}123'
     * @param params 模板参数
     * @return this
     */
    default IException appendError(String template, Object... params) {
        String err = StrUtil.format(template, params);
        return this.appendErrors(err);
    }


    // /**
    //  * cell.swimlane.env.hostname.ip
    //  */
    // default String getOctoInfo() {
    //     String defstr = "default";
    //     return StrUtil.format("{}.{}.{}.{}.{}", ObjectUtil.defaultIfNull(ProcessInfoUtil.getCell(), defstr),
    //             ObjectUtil.defaultIfNull(ProcessInfoUtil.getSwimlane(), defstr),
    //             ProcessInfoUtil.getHostEnv(),
    //             ProcessInfoUtil.getHostNameInfoByIp(),
    //             ProcessInfoUtil.getLocalIpV4());
    // }

}
