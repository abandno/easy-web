package com.nisus.baotool.starter.modules.base;

import com.nisus.baotool.starter.consts.Const;
import lombok.Data;
import org.springframework.boot.Banner;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 *
 * @author L&J
 * @date 2022/1/1 11:01 下午
 */
@Data
@ConfigurationProperties(prefix = BaseProperties.PREFIX)
public class BaseProperties {
    public static final String PREFIX = Const.EASY_WEB;

    // /**
    //  * easy-web base packages. (分割符 ",; \t\n")
    //  */
    // private String basePackages;

    /**
     * Easy Web banner, 默认 'console'
     */
    private Banner.Mode bannerMode = Banner.Mode.CONSOLE;

}
