package com.nisus.baotool.starter.modules.base.datesede;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 不同于一般配置, 我这里拿到spring里默认配置的ObjectMapper, 在其基础上增强配置.
 *
 * see: https://blog.csdn.net/qq_38132283/article/details/89339817
 * @author dafei
 * @version 0.1
 * @date 2020/9/8 14:24
 */
@Slf4j
@Configuration
public class DateSedeObjectMapperConfig {

    // private static final JsonSerializer<Object> DateTime2TimestampJsonSerializer = new DateTime2TimestampJsonSerializer();


    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void configObjectMapper() {
        // 注入 MyBeanSerializerModifier , 实现各类型NULL值变初始化值
        // SerializerFactory serializerFactory = objectMapper.getSerializerFactory().withSerializerModifier(new MyBeanSerializerModifier());
        // objectMapper.setSerializerFactory(serializerFactory); // !! 上面代码是生成新的实例了, 所以, 这里要重新set才有效

        // LocalDateTime 相关配置  Date 类型用的默认, 但需要配置文件里配置格式和时区 spring.jackson.date-format
        // objectMapper.registerModule(new Jdk8Module());
        // JavaTimeModule module = new JavaTimeModule();
        // module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern)));
        // module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern)));
        // module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN))); // "yyyy-MM-dd"
        // module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN))); // "HH:mm:ss"
        // objectMapper.registerModule(module);

        SimpleModule module = new SimpleModule();

        // 作为补充, 针对 Map,List 容器类型的日期类型元素, 这是BeanSerializerModifier的盲区
        // module.addSerializer(LocalDateTime.class, DateTime2TimestampJsonSerializer);
        // module.addSerializer(LocalDate.class, DateTime2TimestampJsonSerializer);
        // module.addSerializer(LocalTime.class, DateTime2TimestampJsonSerializer);
        // module.addSerializer(Date.class, DateTime2TimestampJsonSerializer);

        // module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        module.addDeserializer(LocalDateTime.class, new MyLocalDateTimeDeserializer());
        module.addDeserializer(LocalDate.class, new MyLocalDateDeserializer());
        module.addDeserializer(LocalTime.class, new MyLocalTimeDeserializer());
        module.addDeserializer(Date.class, new MyDateDeserializer());

        objectMapper.registerModule(module);

        // 是否 Date 序列化成 时间戳
        // objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        // objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        /*
          long类型前端精度丢失问题（超过53位二进制数），
          建议在需要转换的字段上加注解 @JsonFormat(shape = JsonFormat.Shape.STRING)
          不做整体数值转换，防止无需转换的long类型也转为string
         */

    }

    //
    // 日期时间 Json Deserializer
    // 与url日期时间参数规则一致 (由 GlobalDateTimeConverterConfig 配置)
    //

    public static class MyLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String text = p.getText();
            return DateTimeMsgConverterUtil.toLocalDateTime(text);
        }
    }

    public static class MyLocalDateDeserializer extends JsonDeserializer<LocalDate> {

        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String text = p.getText();
            return DateTimeMsgConverterUtil.toLocalDate(text);
        }
    }

    public static class MyLocalTimeDeserializer extends JsonDeserializer<LocalTime> {

        @Override
        public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String text = p.getText();
            return DateTimeMsgConverterUtil.toLocalTime(text);
        }
    }

    public static class MyDateDeserializer extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String text = p.getText();
            return DateTimeMsgConverterUtil.toDate(text);
        }
    }



    /**
     * 日期序列化是统一转成时间戳
     */
    public static class DateTime2TimestampJsonSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value instanceof LocalDateTime) {
                long l = LocalDateTimeUtil.toEpochMilli((LocalDateTime) value);
                gen.writeNumber(l);
            } else if (value instanceof LocalDate) {
                long l = LocalDateTimeUtil.toEpochMilli((LocalDate) value);
                gen.writeNumber(l);
            } else if (value instanceof Date) {
                long l = ((Date) value).getTime();
                gen.writeNumber(l);
            } else if (value instanceof LocalTime) {
                // 无法转时间戳, 无特殊情况不要用. 这里转成 "HH:mm:ss"
                String format = ((LocalTime) value).format(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN));
                gen.writeString(format);
            } else {
                // 不识别的
                // gen.writeString(value == null ? "" : value.toString());
                gen.writeNumber(0);
            }
        }
    }

}
