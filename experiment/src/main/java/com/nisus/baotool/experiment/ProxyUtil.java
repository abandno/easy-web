package com.nisus.baotool.experiment;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;

/**
 * <p>
 *
 * @author L&J
 * @version 0.1
 * @since 2022/11/5 02:59
 */
@Slf4j
public abstract class ProxyUtil {

    /**
     * 查询当前实例的代理 proxy, 可能就是自己
     * @param k
     * @param <T>
     * @return
     */
    public static <T> T getProxy(T k) {
        // 要求严格, 必须是 Instanceof SpringProxy
        if (AopUtils.isAopProxy(k) || AopUtils.isCglibProxy(k) || AopUtils.isJdkDynamicProxy(k)) {
            return k;
        }

        /*
         @Configuration 的类, 上面条件不满足
         Configuration 可以简单理解分别注入了 Configuration 实例 和 Component 实例
         本身是代理类, 通过 this.getClass 查找是找不到的
         '$$EnhancerBySpringCGLIB$$'
        */
        String clazzName = k.getClass().getName();
        if (clazzName.contains("$$EnhancerBySpringCGLIB$$")) {
            String originClazzName = clazzName.substring(0, clazzName.indexOf("$$EnhancerBySpringCGLIB$$"));
            try {
                Class<?> originClass = Class.forName(originClazzName);
                Object bean = SpringUtil.getBean(originClass);
                return (T) bean;
            } catch (Exception e) {
                log.info("SpringUtil.getBean by CGLIB origin class 异常: {} {} {}", clazzName, originClazzName, e.getMessage());
            }
        }

        T bean = null;
        try {
            bean = (T) SpringUtil.getBean(k.getClass());
            return bean;
        } catch (Exception e) {
            log.error("SpringUtil.getBean 异常: {}, 将返回原对象", k, e);
            return k;
        }
    }

}
