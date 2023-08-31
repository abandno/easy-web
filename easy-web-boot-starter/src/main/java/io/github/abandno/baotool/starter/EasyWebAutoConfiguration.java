package io.github.abandno.baotool.starter;

import cn.hutool.extra.spring.SpringUtil;
import io.github.abandno.baotool.starter.modules.base.BaseProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.Banner;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

/**
 * <p>
 *
 * @author L&J
 * @date 2021/10/10 10:56 下午
 */
@Slf4j
@Import({
        SpringUtil.class, // 默认引入 hutool 的 SpringUtil
})
@ComponentScan("com.nisus.baotool.starter.modules.base")
@Configuration
// @EnableConfigurationProperties(BaseProperties.class) // 没用??
public class EasyWebAutoConfiguration implements InitializingBean,BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    private static final String BANNER = "\n    ,---,.                                                      .---.                     \n" +
            "  ,'  .' |                                                     /. ./|            ,---,    \n" +
            ",---.'   |                                                 .--'.  ' ;          ,---.'|    \n" +
            "|   |   .'               .--.--.                          /__./ \\ : |          |   | :    \n" +
            ":   :  |-,   ,--.--.    /  /    '       .--,          .--'.  '   \\' .   ,---.  :   : :    \n" +
            ":   |  ;/|  /       \\  |  :  /`./     /_ ./|         /___/ \\ |    ' '  /     \\ :     |,-. \n" +
            "|   :   .' .--.  .-. | |  :  ;_    , ' , ' :         ;   \\  \\;      : /    /  ||   : '  | \n" +
            "|   |  |-,  \\__\\/: . .  \\  \\    `./___/ \\: |          \\   ;  `      |.    ' / ||   |  / : \n" +
            "'   :  ;/|  ,\" .--.; |   `----.   \\.  \\  ' |           .   \\    .\\  ;'   ;   /|'   : |: | \n" +
            "|   |    \\ /  /  ,.  |  /  /`--'  / \\  ;   :            \\   \\   ' \\ |'   |  / ||   | '/ : \n" +
            "|   :   .';  :   .'   \\'--'.     /   \\  \\  ;             :   '  |--\" |   :    ||   :    | \n" +
            "|   | ,'  |  ,     .-./  `--'---'     :  \\  \\             \\   \\ ;     \\   \\  / /    \\  /  \n" +
            "`----'     `--`---'                    \\  ' ;              '---\"       `----'  `-'----'   \n" +
            "                                        `--`                                              " +
            "\n[easy-web-boot-starter] 为简化 web 开发而生!❤️";
    protected Environment environment;


    // 1
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    // 1 -> 我 -> 2
    @Override
    public void afterPropertiesSet() throws Exception {
        // 临时用 (没法注册进去)
        BaseProperties baseProperties = Binder.get(this.environment).bind(BaseProperties.PREFIX, BaseProperties.class).orElse(new BaseProperties());

        Banner.Mode bannerMode = baseProperties.getBannerMode();
        switch (bannerMode) {
            case CONSOLE:
                System.out.println(BANNER);
                break;
            case LOG:
                log.info(BANNER);
                break;
            case OFF:
                break;
            default:
                break;
        }

    }

    // 2
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        /*
         * 默认强制自动扫描 com.sankuai.groceryrisk.common.easyweb.base
         */
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.scan("com.sankuai.groceryrisk.common.easyweb.base");
    }

    // 3
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BaseProperties baseProperties = Binder.get(this.environment).bind(BaseProperties.PREFIX, BaseProperties.class).orElse(new BaseProperties());
        beanFactory.registerSingleton("easyWebBaseProperties", baseProperties);
    }





}
