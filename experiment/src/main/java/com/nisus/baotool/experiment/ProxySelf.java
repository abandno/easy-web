package com.nisus.baotool.experiment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 注意: 依赖 cn.hutool.extra.spring.SpringUtil, 必须被注入了 (easy-web-boot-starter 自动导入了).
 * 注意: @Configuration 注解的类, 只能拿到普通 Component 实例, 不拿配置实例.
 * @author L&J
 * @version 0.1
 * @since 2022/2/22 4:11 下午
 */
public interface ProxySelf<T> {
    Logger log =  LoggerFactory.getLogger(ProxySelf.class);
    Map<Object, Object> PROXY_CACHE = new ConcurrentHashMap<>();

    default T self() {
        Object o = PROXY_CACHE.computeIfAbsent(this, k -> {
            return ProxyUtil.getProxy(k);
        });
        return (T) o;
    }


}
