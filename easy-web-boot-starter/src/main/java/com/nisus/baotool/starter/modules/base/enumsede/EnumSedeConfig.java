package com.nisus.baotool.starter.modules.base.enumsede;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.nisus.baotool.webutil.jackson.de.EnumDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

/**
 * <p>
 *
 * @author L&J
 * @version 0.1
 * @since 2022/6/28 11:43 上午
 */
@Slf4j
@Configuration
public class EnumSedeConfig implements WebMvcConfigurer {

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void configObjectMapper() {
        SimpleModule module = new SimpleModule();

        // 这里虽然 new 了实例, 运行中, 会先判断它是否是 ContextualDeserializer, 是, 则调用它的 createContextual() 新生成实例覆盖之
        module.addDeserializer(Enum.class, new EnumDeserializer());

        objectMapper.registerModule(module);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new EnumConverterFactory());
    }



}
