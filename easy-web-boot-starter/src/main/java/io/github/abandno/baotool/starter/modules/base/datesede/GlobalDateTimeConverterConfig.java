package io.github.abandno.baotool.starter.modules.base.datesede;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * url参数日期类型反序列化支持
 * ! 时间戳暂仅支持毫秒转 !
 *
 * https://juejin.im/post/6844904177479450632
 */
@Configuration
public class GlobalDateTimeConverterConfig {

    /**
     * LocalDate转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @SuppressWarnings("NullableProblems")
            @Override
            public LocalDate convert(String source) {
                return DateTimeMsgConverterUtil.toLocalDate(source);
            }
        };
    }

    /**
     * LocalDateTime转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @SuppressWarnings("NullableProblems")
            @Override
            public LocalDateTime convert(String source) {
                return DateTimeMsgConverterUtil.toLocalDateTime(source);
            }
        };
    }

    /**
     * LocalDate转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @SuppressWarnings("NullableProblems")
            @Override
            public LocalTime convert(String source) {
                return DateTimeMsgConverterUtil.toLocalTime(source);
            }
        };
    }

    /**
     * Date转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, Date> dateConverter() {
        return new Converter<String, Date>() {
            @SuppressWarnings("NullableProblems")
            @Override
            public Date convert(String source) {
                return DateTimeMsgConverterUtil.toDate(source);
            }
        };
    }

    // /**
    //  * Json序列化和反序列化转换器，用于转换Post请求体中的json以及将我们的对象序列化为返回响应的json
    //  */
    // @Bean
    // public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    //     return builder -> builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN)))
    //             .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
    //             .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))
    //             .serializerByType(Long.class, ToStringSerializer.instance)
    //             .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN)))
    //             .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
    //             .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
    // }



}

